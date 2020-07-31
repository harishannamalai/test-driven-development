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
 * Test Cases for User Story/Use Case 4
 * <p>
 * When​, I return one book to the library
 * Then​, the book is removed from my borrowed list
 * And​, the library reflects the updated stock of the book
 * <p>
 * When​, I return both books to the library
 * Then​, my borrowed list is empty
 * And​, the library reflects the updated stock of the books
 */
@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Log4j2
public class UserCanReturnBooksToLibraryTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static User testUser;
    private static Book book1;
    private static Book book2;

    @Test
    @Order(1)
    public void init() throws Exception {
        User user1 = TestUtils.createUser(mockMvc, mapper, TestUtils.createUserInfo("Rajesh", "rajesh@gmail.com", true));
        Assert.isTrue(user1.getUserId() > 0, "User Id, Not Generated");
        testUser = user1;
    }

    @Test
    @Order(2)
    public void WhenTestUser_ThenBorrowsTwoBooks() throws Exception{
        book1 = TestUtils.addBookToLibrary(mockMvc, mapper, TestUtils.createBookInfo("My History - U4  C1", "Chetan B", 1));
        Assert.isTrue(book1.getBookId() > 0, "Book Id, Not Generated");

        book2 = TestUtils.addBookToLibrary(mockMvc, mapper, TestUtils.createBookInfo("My History - U4  C2", "Chetan B", 1));
        Assert.isTrue(book2.getBookId() > 0, "Book Id, Not Generated");

        CatalogRequest request1 = new CatalogRequest();
        request1.setAction(CatalogUserAction.BORROW);
        request1.setBookId(book1.getBookId());

        CatalogRequest request2 = new CatalogRequest();
        request2.setAction(CatalogUserAction.BORROW);
        request2.setBookId(book2.getBookId());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/catalog/user/" + testUser.getUserId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(request1)))
                .andReturn();

        Assert.notNull(mvcResult, "Borrow Action Failed.");

        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/catalog/user/" + testUser.getUserId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(request2)))
                .andReturn();

        List<CatalogResponseItem> items = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(!items.isEmpty(),"Books Should be returned!");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == book1.getBookId()), "Borrowed Book1 Not Found in Response.");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == book2.getBookId()), "Borrowed Book2 Not Found in Response.");

    }

    @Test
    @Order(3)
    public void WhenUserHasTwoBooks_ThenReturnsOne_ExpectOneBookInLibrary() throws Exception{
        log.info("--When User Has Two Books Then Returns One Book Book is in the Library--");
        log.info("------------- Start-----------------------------------------------------");

        CatalogRequest request = new CatalogRequest();
        request.setAction(CatalogUserAction.RETURN);
        request.setBookId(book1.getBookId());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/catalog/user/" + testUser.getUserId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(request)))
                .andReturn();

        List<CatalogResponseItem> items = mapper.readValue(mvcResult.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(!items.isEmpty(),"User Should Have at least One book after Returning One");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == book2.getBookId()), "Borrowed Book2 Not Found in Response.");

        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/catalog/books"))
                .andReturn();

        items = mapper.readValue(mvcResult.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));
        Assert.isTrue(!items.isEmpty(),"Library Should Have all the Returned Books");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == book1.getBookId()), "Borrowed Book1 Not Found in Library.");

        log.info("------------- End  -----------------------------------------------------");
        log.info("--When User Has Two Books Then Returns One Book Book is in the Library--");
    }

    @Test
    @Order(3)
    public void WhenUserHasOneBooks_ThenReturnsOne_ExpectBorrowedListIsiEmpty() throws Exception{
        log.info("--When User Has Last Book Then Returns Last Book Now Borrowed List Empty");
        log.info("------------- Start-----------------------------------------------------");

        CatalogRequest request = new CatalogRequest();
        request.setAction(CatalogUserAction.RETURN);
        request.setBookId(book2.getBookId());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/catalog/user/" + testUser.getUserId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(request)))
                .andReturn();

        List<CatalogResponseItem> items = mapper.readValue(mvcResult.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));
        Assert.isTrue(items.isEmpty(),"User Should Have No Books after Returning Last One");

        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/catalog/books"))
                .andReturn();

        items = mapper.readValue(mvcResult.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));
        Assert.isTrue(!items.isEmpty(),"Library Should Have all the Returned Books");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == book1.getBookId()), "Borrowed Book1 Not Found in Library.");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == book2.getBookId()), "Borrowed Book2 Not Found in Library.");

        log.info("------------- End  -----------------------------------------------------");
        log.info("--When User Has Last Book Then Returns Last Book Now Borrowed List Empty");
    }
}
