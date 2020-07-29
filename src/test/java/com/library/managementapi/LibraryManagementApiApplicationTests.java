package com.library.managementapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.managementapi.constants.CatalogUserAction;
import com.library.managementapi.entities.Book;
import com.library.managementapi.entities.User;
import com.library.managementapi.models.BookInfo;
import com.library.managementapi.models.CatalogRequest;
import com.library.managementapi.models.CatalogResponseItem;
import com.library.managementapi.models.UserInfo;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Log4j2
class LibraryManagementApiApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private User testUser;

    @BeforeAll
    public void init() throws Exception{
        User user = this.createUser(this.createUserInfo("Harish", "cerium11@gmail.com", true));
        Assert.isTrue(user.getUserId() > 0, "User Id, Not Generated");
        log.info(user);
        testUser = user;
    }

    @Test
    @Order(1)
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
    @Order(2)
    public void WhenOneBookIsAddedToLibrary_ExpectNewInResponse() throws Exception{
        log.info("------------- When Library Is Not Empty Then should have Books ---------");
        log.info("------------- Start-----------------------------------------------------");

        Book book = this.addBookToLibrary(this.createBookInfo("Star Wars", "Leo Man", 1));
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

    @Test
    @Order(3)
    public void WhenBooksAreAvailable_ThenBookIsAddedAndRemovedFromCatalogResponse() throws Exception{
        log.info("---When Book are available Then Book is Borrowed and Removed -----------");
        log.info("------------- Start-----------------------------------------------------");

        Book book = this.addBookToLibrary(this.createBookInfo("My History", "Chetan B", 1));
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

        Assert.isTrue(!items.isEmpty(),"Empty Library should have zero Books, but books are returned!");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).noneMatch(p -> p == book.getBookId()), "Borrowed Book Found in Response.");
        log.info("------------- End  -----------------------------------------------------");
        log.info("---When Book are available Then Book is Borrowed and Removed -----------");
    }

    @Test
    @Order(4)
    public void WhenSameCopyIsAvailableTwice_ThenBookIsAddedAndCatalogHasAtleastOne() throws Exception{
        log.info("---When Same book available twice one is added to user and one available");
        log.info("------------- Start-----------------------------------------------------");

        Book book = this.addBookToLibrary(this.createBookInfo("My History", "Chetan B", 1));
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

        Assert.isTrue(!items.isEmpty(),"Empty Library should have zero Books, but books are returned!");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).noneMatch(p -> p == book.getBookId()), "Borrowed Book Not Found in Response.");
        log.info("------------- End  -----------------------------------------------------");
        log.info("---When Same book available twice one is added to user and one available");
    }

    @Test
    @Order(5)
    public void WhenHasBorrowedTwoBooks_ThenNewBookIsNotCheckedOut() throws Exception{
        log.info("---When Same book available twice one is added to user and one available");
        log.info("------------- Start-----------------------------------------------------");

        Book book = this.addBookToLibrary(this.createBookInfo("World War", "Anthony G", 1));
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

        Assert.isTrue(!items.isEmpty(),"Empty Library should have zero Books, but books are returned!");
        Assert.isTrue(items.stream().map(CatalogResponseItem::getBookId).noneMatch(p -> p == book.getBookId()), "Borrowed Book Not Found in Response.");
        log.info("------------- End  -----------------------------------------------------");
        log.info("---When Same book available twice one is added to user and one available");
    }

    @Test
    @Order(6)
    public void WhenUserReturnsBook_ThenBookAddedToCatalog() throws Exception{
        log.info("---When Same book available twice one is added to user and one available");
        log.info("------------- Start-----------------------------------------------------");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/catalog/user/" + testUser.getUserId()))
                .andReturn();

        List<CatalogResponseItem> items = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        CatalogRequest request = new CatalogRequest();
        request.setAction(CatalogUserAction.RETURN);
        request.setBookId(items.get(0).getBookId());

        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/catalog/user/" + testUser.getUserId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(request)))
                .andReturn();

        List<CatalogResponseItem> afterReturn = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(!afterReturn.isEmpty(),"Empty Library should have zero Books, but books are returned!");
        Assert.isTrue(afterReturn.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == items.get(0).getBookId()), "Borrowed Book Not Found in Response.");
        log.info("------------- End  -----------------------------------------------------");
        log.info("---When Same book available twice one is added to user and one available");
    }

    @Test
    @Order(7)
    public void WhenOnlyOnceCopyIsVailable_ThenBorrowedBookIsRemovedFromCatalog() throws Exception{
        log.info("---When Same book available twice one is added to user and one available");
        log.info("------------- Start-----------------------------------------------------");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/catalog/user/" + testUser.getUserId()))
                .andReturn();

        List<CatalogResponseItem> items = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        CatalogRequest request = new CatalogRequest();
        request.setAction(CatalogUserAction.RETURN);
        request.setBookId(items.get(0).getBookId());

        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/catalog/user/" + testUser.getUserId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(request)))
                .andReturn();

        List<CatalogResponseItem> afterReturn = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CatalogResponseItem.class));

        Assert.isTrue(!afterReturn.isEmpty(),"Empty Library should have zero Books, but books are returned!");
        Assert.isTrue(afterReturn.stream().map(CatalogResponseItem::getBookId).anyMatch(p -> p == items.get(0).getBookId()), "Borrowed Book Not Found in Response.");
        log.info("------------- End  -----------------------------------------------------");
        log.info("---When Same book available twice one is added to user and one available");
    }

    private Book addBookToLibrary(BookInfo info) throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/books/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(info)))
                .andReturn();

        return mapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class);
    }

    private BookInfo createBookInfo(String title, String author, int revision){
        BookInfo info = new BookInfo();
        info.setAuthor(author);
        info.setTitle(title);
        info.setRevision(revision);
        return info;
    }

    private User createUser(UserInfo info) throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/users/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(info)))
                .andReturn();

        return mapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
    }

    private UserInfo createUserInfo(String name, String email, boolean active){

        UserInfo info = new UserInfo();
        info.setName(name);
        info.setEmail(email);
        info.setActive(active);
        return info;
    }
}
