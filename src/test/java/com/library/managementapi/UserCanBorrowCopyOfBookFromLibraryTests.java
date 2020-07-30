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

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Log4j2
public class UserCanBorrowCopyOfBookFromLibraryTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static User testUser1;
    private static User testUser2;

    @Test
    @Order(1)
    public void init() throws Exception {
        User user1 = TestUtils.createUser(mockMvc, mapper, TestUtils.createUserInfo("Harish Annamalai", "cerium11@gmail.com", true));
        Assert.isTrue(user1.getUserId() > 0, "User Id, Not Generated");
        log.info(user1);
        testUser1 = user1;

        User user2 = TestUtils.createUser(mockMvc, mapper, TestUtils.createUserInfo("Girish Annamalai", "girish@gmail.com", true));
        Assert.isTrue(user2.getUserId() > 0, "User Id, Not Generated");
        log.info(user2);
        testUser2 = user2;
    }


    @Test
    @Order(2)
    public void WhenMoreThanOneCopy_ThenUserBorrowsAndAtleastOneCopyIsAvailable() throws Exception{
        log.info("--When More than one Copy is available at least One Copy is Left -------");
        log.info("------------- Start-----------------------------------------------------");

        Book book1 = TestUtils.addBookToLibrary(mockMvc, mapper, TestUtils.createBookInfo("My History - U3 C1", "Chetan B", 1));
        Assert.isTrue(book1.getBookId() > 0, "Book Id, Not Generated");

        Book book2 = TestUtils.addBookToLibrary(mockMvc, mapper, TestUtils.createBookInfo("My History - U3 C1", "Chetan B", 1));
        Assert.isTrue(book2.getBookId() > 0, "Book Id, Not Generated");

        CatalogRequest request = new CatalogRequest();
        request.setAction(CatalogUserAction.BORROW);
        request.setBookId(book1.getBookId());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/catalog/user/" + testUser1.getUserId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(request)))
                .andReturn();

        List<CatalogResponseItem> items = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(!items.isEmpty(),"Empty Library should have zero Books, but books are returned!");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == book1.getBookId()), "Borrowed Book Not Found in Response.");

        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/catalog/books?bookId="+book2.getBookId()))
                .andReturn();

        items = mapper.readValue(mvcResult.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(!items.isEmpty(),"Library Should Have the Same Copy of Book, but Response is Empty");

        log.info("------------- End  -----------------------------------------------------");
        log.info("--When More than one Copy is available at least One Copy is Left -------");
    }

    @Test
    @Order(3)
    public void WhenMoreThanOneCopy_ThenUserBorrowsSameBookExpectBookNotInBorrowedList() throws Exception{
        log.info("--When More than one Copy is available And Same Book Borrowed Again-----");
        log.info("------------- Start-----------------------------------------------------");

        Book book1 = TestUtils.addBookToLibrary(mockMvc, mapper, TestUtils.createBookInfo("My History - U3 C2", "Chetan B", 1));
        Assert.isTrue(book1.getBookId() > 0, "Book Id, Not Generated");

        Book book2 = TestUtils.addBookToLibrary(mockMvc, mapper, TestUtils.createBookInfo("My History - U3 C2", "Chetan B", 1));
        Assert.isTrue(book2.getBookId() > 0, "Book Id, Not Generated");

        CatalogRequest request = new CatalogRequest();
        request.setAction(CatalogUserAction.BORROW);
        request.setBookId(book1.getBookId());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/catalog/user/" + testUser2.getUserId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(request)))
                .andReturn();

        List<CatalogResponseItem> items = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(!items.isEmpty(),"Empty Library should have zero Books, but books are returned!");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == book1.getBookId()), "Borrowed Book Found in Response.");


        CatalogRequest request2 = new CatalogRequest();
        request2.setAction(CatalogUserAction.BORROW);
        request2.setBookId(book2.getBookId());

        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/catalog/user/" + testUser2.getUserId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(request2)))
                .andReturn();

        items = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(!items.isEmpty(),"User Borrowed List must have one Book");
        Assert.isTrue(items.size() == 1, "Borrowed Book List is not On2");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == book1.getBookId()), "Borrowed Book Found in Response.");

        log.info("------------- End  -----------------------------------------------------");
        log.info("--When More than one Copy is available And Same Book Borrowed Again-----");
    }

    @Test
    @Order(3)
    public void WhenOnlyOneCopyIsAvailable_ThenBorrowed_ExpectBookNotInLibrary() throws Exception{
        log.info("--When Only one copy is available then the book is not in library ------");
        log.info("------------- Start-----------------------------------------------------");

        Book book1 = TestUtils.addBookToLibrary(mockMvc, mapper, TestUtils.createBookInfo("My History - U3 C3", "Chetan B", 1));
        Assert.isTrue(book1.getBookId() > 0, "Book Id, Not Generated");

        CatalogRequest request = new CatalogRequest();
        request.setAction(CatalogUserAction.BORROW);
        request.setBookId(book1.getBookId());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/catalog/user/" + testUser1.getUserId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(request)))
                .andReturn();

        List<CatalogResponseItem> items = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(!items.isEmpty(),"Empty Library should have zero Books, but books are returned!");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == book1.getBookId()), "Borrowed Book Found in Response.");

        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/catalog/books?bookId="+book1.getBookId()))
                .andReturn();

        items = mapper.readValue(mvcResult.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(items.isEmpty(),"Empty Library should have zero Books, but books are returned!");

        log.info("------------- End  -----------------------------------------------------");
        log.info("--When Only one copy is available then the book is not in library ------");
    }
}
