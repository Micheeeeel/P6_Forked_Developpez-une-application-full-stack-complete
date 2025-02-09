package com.openclassrooms.mddapi.configuration;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.openclassrooms.mddapi.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.openclassrooms.mddapi.service.JwtUserDetailsService;
@Component

public class jwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private jwtTokenUtil jwtTokenUtil;

    // filtre d'inspection des requêtes HTTP afin d'en extraire le token JWT, de le valider et de charger les détails de l'utilisateur associé
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String userLoginFromToken = null;
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                // La conformité du jeton JWT est ensuite vérifié et ses données récupérées
                DecodedJWT jwt = jwtTokenUtil.verifyToken(token);
                userLoginFromToken = jwt.getSubject();

                // Une fois qu'on a le token, on doit le valider avec l'utilisateur auquel il se réfère
                if(
                        userLoginFromToken != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {  // Si aucun utilisateur n'est actuellement authentifié, cette méthode renverra null.Le SecurityContext sera utilisé pour stocker des détails sur la sécurité concernant l'utilisateur ou la requête en cours (comme son nom d'utilisateur, ses rôles, ses privilèges, etc.).

                    // Chargement des détails de l'utilisateur
                    CustomUserDetails userDetails = this.jwtUserDetailsService.loadUserByLogin(userLoginFromToken);

                    // On vérifie que le token n'est pas expiré et que son subjectcorrespond bien à l'email de l'utilisateur
                    if(jwtTokenUtil.validateToken(token, userDetails, userLoginFromToken)){

                        // une instance UsernamePasswordAuthenticationToken est créée avec les détails de l'utilisateur, ses rôles et autorisations
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        // Ajout des détails de l'authentification
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // L'authentification est définie dans le contexte de sécurité : signifie que l'utilisateur actuel est considéré comme authentifié
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    }

                }

            } catch (Exception e) {
                System.out.println("Unable to get or validate JWT Token or JWT Token has expired");
            }
        } else{
            logger.warn("Header with Bearer not found");
        }

        filterChain.doFilter(request, response);
    }
}

