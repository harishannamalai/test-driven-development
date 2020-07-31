package com.library.managementapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.managementapi.constants.CatalogUserAction;
import com.library.managementapi.entities.Book;
import com.library.managementapi.entities.User;
import com.library.managementapi.models.CatalogRequest;
import com.library.managementapi.models.CatalogResponseItem;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Test Cases for User Story/Use Case 2
 * <p>
 * When​, I choose a book to add to my borrowed list
 * Then​, the book is added to my borrowed list
 * And​, the book is removed from the library
 */


@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Log4j2
public class UserCanBorrowBooksFromLibraryTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static User testUser;

    @Test
    @Order(0)
    public void init() throws Exception {
        User user = TestUtils.createUser(mockMvc, mapper, TestUtils.createUserInfo("Harish A", "cerium11@gmail.com", true));
        Assert.isTrue(user.getUserId() > 0, "User Id, Not Generated");
        log.info(user);
        testUser = user;
    }


    @Test
    @Order(2)
    public void WhenBooksAreAvailable_ThenBookIsAddedAndRemovedFromCatalogResponse() throws Exception{
        log.info("---When Book are available Then Book is Borrowed and Removed -----------");
        log.info("------------- Start-----------------------------------------------------");

        Book book = TestUtils.addBookToLibrary(mockMvc, mapper, TestUtils.createBookInfo("My History - U2", "Chetan B", 1));
        Assert.isTrue(book.getBookId() > 0, "Book Id, Not Generated");

        CatalogRequest request = new CatalogRequest();
        request.setAction(CatalogUserAction.BORROW);
        request.setBookId(book.getBookId());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/catalog/user/" + testUser.getUserId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(request)))
                .andReturn();

        List<CatalogResponseItem> items = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(!items.isEmpty(), "Empty Library should have zero Books, but books are returned!");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == book.getBookId()), "Borrowed Book Not Found in Response.");

        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/catalog/books?bookId=" + book.getBookId()))
                .andReturn();

        items = mapper.readValue(mvcResult.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(items.isEmpty(), "Empty Library should have zero Books, but books are returned!");

        log.info("------------- End  -----------------------------------------------------");
        log.info("---When Book are available Then Book is Borrowed and Removed -----------");
    }
}
