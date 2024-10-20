package edu.eci.cvds.task_back.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY="CVDSLABORATORIO7ABCDEFGHIJKLMNOPQRSTUVWXYZJAMES";

    /**
     * Genera un token JWT para el usuario proporcionado.
     * @param user Detalles del usuario para el cual se generará el token.
     * @return String que representa el token JWT generado.
     */
    public String getToken(UserDetails user){
        return getToken(new HashMap<>(),user);
    }

    /**
     * Genera un token JWT para el usuario con reclamos adicionales.
     * @param extraClaims Mapa de reclamos adicionales a incluir en el token.
     * @param user  Detalles del usuario para el cual se generará el token.
     * @return String que representa el token JWT generado.
     */
    public String getToken(Map<String,Object> extraClaims, UserDetails user){
        try{
        extraClaims.put("role", user
                .getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found")) .getAuthority());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }catch(Exception e){

        }
        return "";
    }

    /**
     * Obtiene la clave secreta utilizada para firmar el token JWT.
     * @return Key que representa la clave utilizada para firmar el token.
     */
    public Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     * @param token Token del cual se extraerá el nombre de usuario.
     * @return String que representa el nombre de usuario extraído del token.
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Válida si el token JWT es válido.
     * @param token      Token que se va a validar.
     * @param userDetails Detalles del usuario que se compararán con el token.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    /**
     * Obtiene todos los reclamos del token JWT.
     * @param token Token del cual se obtendrán los reclamos.
     * @return Claims que representan los reclamos del token.
     */
    private Claims getAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene un reclamo específico del token JWT usando un resolver de reclamos.
     * @param token Token del cual se extraerá el reclamo.
     * @param claimsResolver Función que procesará los reclamos y devolverá el valor requerido.
     * @param <T> Tipo del valor a retornar.
     * @return Valor del reclamo solicitado.
     */
    public <T> T getClaim(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims=getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Obtiene la fecha de expiración del token JWT.
     * @param token Token del cual se extraerá la fecha de expiración.
     * @return Date que representa la fecha de expiración del token.
     */
    private Date getExpiration(String token)
    {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * Verifica si el token JWT ha expirado.
     * @param token Token que se va a verificar.
     * @return true si el token ha expirado, false si es válido.
     */
    private boolean isTokenExpired(String token)
    {
        return getExpiration(token).before(new Date());
    }
}
