package ru.kpfu.itis.rodsher.security.provider;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.access.intercept.RunAsUserToken;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.models.Role;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.repositories.UsersRepository;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.security.details.UserDetailsServiceImpl;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Optional;

@Component
@PropertySource("classpath:application.properties")
public class CustomCountryAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private Environment environment;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        try {
            InetAddress ip = InetAddress.getByName(details.getRemoteAddress());
            //
            ip = InetAddress.getByName("78.107.21.245");
            //
            System.out.println(ip);
            DatabaseReader databaseReader = new DatabaseReader.Builder(new File(environment.getProperty("geoip.db.location"))).build();
            CityResponse cityResponse = databaseReader.city(ip);
            System.out.println(cityResponse.getCity().getName() + " " + cityResponse.getCountry().getName());
            if(!cityResponse.getCountry().getName().equals(environment.getProperty("auth.country"))) {
                throw new BadCredentialsException("Unacceptable country.");
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
            if(passwordEncoder.matches((String) authentication.getCredentials(), userDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                return new CountryAuthentication(userDetails, true);
            }
            throw new BadCredentialsException("User not found.");
        } catch (IOException | GeoIp2Exception e) {
            throw new AuthenticationServiceException("Checking IP exception.");
        }
//        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
