package com.library.managementapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.managementapi.entities.Book;
import com.library.managementapi.entities.User;
import com.library.managementapi.models.BookInfo;
import com.library.managementapi.models.UserInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Log4j2
public class TestUtils {

    public static Book addBookToLibrary(MockMvc mockMvc, ObjectMapper mapper, BookInfo info) throws Exception {
        log.info("Invoking API for creating books: " + info);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/books/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(info)))
                .andReturn();

        return mapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class);
    }

    public static BookInfo createBookInfo(String title, String author, int revision){
        BookInfo info = new BookInfo();
        info.setAuthor(author);
        info.setTitle(title);
        info.setRevision(revision);
        return info;
    }

    public static User createUser(MockMvc mockMvc, ObjectMapper mapper, UserInfo info) throws Exception {
        log.debug("Invoking API for creating users: " + info);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/users/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(info)))
                .andReturn();

        return mapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
    }

    public static UserInfo createUserInfo(String name, String email, boolean active){

        UserInfo info = new UserInfo();
        info.setName(name);
        info.setEmail(email);
        info.setActive(active);
        return info;
    }
}
