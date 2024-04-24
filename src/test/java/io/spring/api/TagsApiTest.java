package io.spring.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.spring.application.TagsQueryService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TagsApi.class)
public class TagsApiTest {

    @Autowired private MockMvc mvc;

    @MockBean private TagsQueryService tagsQueryService;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    public void should_return_all_tags_success() {
        List<String> expectedTags = Arrays.asList("tag1", "tag2", "tag3");
        when(tagsQueryService.allTags()).thenReturn(expectedTags);

        given()
                .when()
                .get("/tags")
                .then()
                .statusCode(200)
                .body("tags", equalTo(expectedTags));
    }

    @Test
    public void should_return_empty_list_when_no_tags() {
        when(tagsQueryService.allTags()).thenReturn(Arrays.asList());

        given()
                .when()
                .get("/tags")
                .then()
                .statusCode(200)
                .body("tags", equalTo(Arrays.asList()));
    }
}
