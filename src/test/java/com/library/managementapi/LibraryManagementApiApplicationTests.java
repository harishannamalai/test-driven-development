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
import org.springframework.test.context.event.annotation.BeforeTestClass;
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

    @Test public void contextLoads(){

    }
}
