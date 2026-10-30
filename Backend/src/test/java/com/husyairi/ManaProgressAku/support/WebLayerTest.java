package com.husyairi.ManaProgressAku.support;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.husyairi.ManaProgressAku.Config.JwtAuthenticationEntryPoint;
import com.husyairi.ManaProgressAku.Config.SecurityConfiguration;
import com.husyairi.ManaProgressAku.DTO.User.CustomUserDetails;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Enums.Role;
import com.husyairi.ManaProgressAku.Service.impl.CustomUserDetailsService;
import com.husyairi.ManaProgressAku.Service.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import({SecurityConfiguration.class, JwtAuthenticationEntryPoint.class})
public abstract class WebLayerTest {

    private static final String TOKEN = "valid-test=token";

    @Autowired
    protected MockMvc mockMvc;

    // What SecurityConfiguration and JwtAuthenticationFilter need in order to start.
    @MockitoBean
    protected JwtService jwtService;

    @MockitoBean
    protected CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    protected AuthenticationProvider authenticationProvider;


    /**
     * Teaches the faked JwtService that TOKEN belongs to a real user with this role,
     * and returns the value for the Authorization header.
     */
    protected String loggedInAs(Role role){
        User user = new User("Test" + role, role.name().toLowerCase() + "@test.com", "HASH", role);

        user.setId(1L);
        CustomUserDetails details = new CustomUserDetails(user);

        when(jwtService.extractUsername(TOKEN)).thenReturn(user.getEmail());
        when(customUserDetailsService.loadUserByUsername(user.getEmail())).thenReturn(details);
        when(jwtService.isTokenValid(TOKEN, any(UserDetails.class))).thenReturn(true);

        return "Bearer " + TOKEN;
    }

    /** Lets test bodies be written with single quotes: json("{'a':1}") gives {"a":1}. */
    protected static String json(String singleQuoted) {
        return singleQuoted.replace('\'', '"');
    }
}
