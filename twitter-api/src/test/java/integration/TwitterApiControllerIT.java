package integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.thomasdacosta.TwitterApiApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TwitterApiApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TwitterApiControllerIT {

	private static EmbededRedisServer server;

	private static HashOperations<String, String, String> hashOperations;
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@BeforeClass
	public static void createData() throws IOException {
		server = new EmbededRedisServer();
		server.setRedisPort(6379);
		server.startRedis();
	}

	@AfterClass
	public static void stop() {
		server.stopRedis();
	}

	@Before
	public void setUp() throws IOException {
		mvc = webAppContextSetup(context).build();

		if (hashOperations == null) {
			hashOperations = redisTemplate.opsForHash();
			insertHashTagData();
		}
	}

	@Test
	public void test01_200() throws Exception {
		mvc.perform(get("/users/azure")).andExpect(jsonPath("$[0].user").value("keshavbeniwal2"))
				.andExpect(status().isOk());
	}

	@Test
	public void test01_400() throws Exception {
		mvc.perform(get("/users/aws")).andExpect(status().isNotFound());
	}

	private void insertHashTagData() {
		hashOperations.put("twitter-users_azure", "keshavbeniwal2", "52448");
		hashOperations.put("twitter-users_azure", "solarwinds", "15579");
		hashOperations.put("twitter-users_azure", "netec", "10031");
		hashOperations.put("twitter-users_azure", "oliver_hoess", "7645");
		hashOperations.put("twitter-users_azure", "dsscreenshot", "7145");
	}

}
