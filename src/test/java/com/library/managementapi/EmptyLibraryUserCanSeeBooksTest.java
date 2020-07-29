package com.library.managementapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.managementapi.entities.Book;
import com.library.managementapi.models.CatalogResponseItem;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Log4j2
public class EmptyLibraryUserCanSeeBooksTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @Order(0)
    public void WhenLibraryIsEmpty_ExpectNoBooksInResponse() throws Exception{
        log.info("------------- When Library Is Empty Expect No Books In Response --------");
        log.info("------------- Start-----------------------------------------------------");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/catalog/books"))
                .andReturn();

        List<CatalogResponseItem> items = mapper.readValue(mvcResult.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(items.isEmpty(),"Empty Library should have zero Books, but books are returned!");
        log.info("------------- End  -----------------------------------------------------");
        log.info("------------- When Library Is Empty Expect No Books In Response --------");
    }

    @Test
    @Order(1)
    public void WhenOneBookIsAddedToLibrary_ExpectNewInResponse() throws Exception{
        log.info("------------- When Library Is Not Empty Then should have Books ---------");
        log.info("------------- Start-----------------------------------------------------");

        Book book = TestUtils.addBookToLibrary(mockMvc, mapper, TestUtils.createBookInfo("Star Wars - U1", "Leo Man", 1));
        Assert.isTrue(book.getBookId() > 0, "Book Id, Not Generated");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/catalog/books"))
                .andReturn();

        List<CatalogResponseItem> items = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(!items.isEmpty(),"Empty Library should have zero Books, but books are returned!");
        log.info("------------- End  -----------------------------------------------------");
        log.info("------------- When Library Is Not Empty Then should have Books ---------");
    }
}
