package com.drw.mongoex.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@WebAppConfiguration
class ProductApplicationIntegrationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Container
	static
	MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));

	@BeforeEach
	public void setup(){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry){
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void Test_get_all_products() throws Exception {
		this.mockMvc.perform(get("/products")).andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(6)));
	}

	@Test
	void Test_insert_many_products() throws Exception {
		String productJsonStr = "[{ \"id\": 7, \"name\": \"Refrigerator\", \"modelNumber\": 107,  \"brand\": \"Samsung\",  \"url\": \"g.jpg\",  \"price\": 15000,  \"category\": [ \"electronics\"]}, " +
				"{ \"id\": 8, \"name\": \"Gas Cooker\", \"modelNumber\": 108,  \"brand\": \"Suga\",  \"url\": \"h.jpg\",  \"price\": 6000,  \"category\": [ \"kitchenware\"]}]";
		this.mockMvc.perform(post("/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productJsonStr)).andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(2)));
	}

	@Test
	void Test_find_product() throws Exception {

		String productJsonStr = "{\"data\": {\"id\": \"4\",\"name\": \"Blender\",\"modelNumber\": \"104\",	\"brand\": \"Phillips\",\"url\": \"/home/Pictures/d.jpg\",\"price\": 1500,\"category\": [\"kitchenware\", \"electronics\"]}, " +
				"\"message\":\"Product Found\",\"status\":200}";
		this.mockMvc.perform(get("/products/4")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(productJsonStr));
	}

	@Test
	void Test_partial_update_product() throws Exception {
		String updateStr = "{\"modelNumber\": 120 ,\"price\": 3500}";
		String productJsonStr = "{\"data\": {\"id\": \"5\",\"name\": \"Oven\",\"modelNumber\": \"120\",\"brand\": \"Suga\",\"url\": \"/home/Pictures/e.jpg\",\"price\": 3500,\"category\": [\"kitchenware\", \"electronics\"]}," +
				"\"message\":\"Products Updated\",\"status\":200}";
		this.mockMvc.perform(patch("/products/5")
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateStr)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(productJsonStr));
	}

	@Test
	void Test_get_all_products_by_page() throws Exception {
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("pageNo","0");
		multiValueMap.add("pageSize","2");
		multiValueMap.add("fields","name,modelNumber");
		multiValueMap.add("sortBy","ASC");
		this.mockMvc.perform(get("/products/page").queryParams(multiValueMap)).andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.items", hasSize(2)));
	}

	@Test
	void Test_delete_by_brand_and_model_number() throws Exception {
		this.mockMvc.perform(delete("/products/brand/Sony/model/106")).andDo(print()).andExpect(status().isOk());
	}
}
