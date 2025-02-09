package com.openclassrooms.mddapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "articles", uniqueConstraints = {
    @UniqueConstraint(columnNames = "title")
})
@Data // Génère les getters et setters.
@NoArgsConstructor  // Génère un constructeur sans paramètre.
@AllArgsConstructor // Génère un constructeur avec un paramètre pour chaque propriété de la classe.
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author; // The user who authored the article

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject; // The subject the article belongs to

    @Column(nullable = false)    // le nom du titre ne peut pas être vide
    private String title;

    @Lob    // permet de stocker de longs textes
    @Column(nullable = false, length = 5000) // adjust length as needed
    private String content;

    @Temporal(TemporalType.TIMESTAMP)   //spécifie que la propriété publishedAt doit être persistée en tant que TIMESTAMP SQL
    private Date publishedAt;

    @PrePersist //méthode de rappel qui sera exécutée juste avant qu'une entité soit initialement persistée
    protected void onCreate() {
        publishedAt = new Date();   //appelée pour définir la valeur de publishedAt à la date et heure actuelles.
    }

    public static Article createNewArticle(String title, String content, Subject subject, User author) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setSubject(subject);
        article.setAuthor(author);
        return article;
    }







}
