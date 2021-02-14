package ru.otus.spring.library.docker.services;

import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.model.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.docker.models.Book;
import ru.otus.spring.library.docker.models.Comment;
import ru.otus.spring.library.docker.models.User;
import ru.otus.spring.library.docker.repositories.UserRepository;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Component
public class SecurityService {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER_18 = "ROLE_USER18+";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String AGE_LIMIT_18 = "18+";

    private UserRepository userRepository;
    private MutableAclService mutableAclService;

    public SecurityService(UserRepository userRepository, MutableAclService mutableAclService) {
        this.userRepository = userRepository;
        this.mutableAclService = mutableAclService;
    }

    // https://www.baeldung.com/get-user-in-spring-security
    public User getUser() {
        Authentication authentication = getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentLogin = authentication.getName();
            User user = userRepository.findByLoginIgnoreCase(currentLogin);
            return user;
        }
        return null;
    }

    public void setBookAcl(Book book) {

        // Retrieve the current principal, in order to know who "owns" this ACL
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PrincipalSid ownerSid = new PrincipalSid(auth);

        // Create the acl_object_identity row
        final ObjectIdentity oid = new ObjectIdentityImpl(Book.class, book.getId());

        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid, Collections.singletonList(ownerSid));
        } catch (NotFoundException nfex) {
            acl = mutableAclService.createAcl(oid);
        }

        // Назначим права владельцу, сюда должен пройти только администратор.
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, ownerSid, true);

        // Проверяем, является ли книга к которой делается комментарий 18+ или нет
        if( book.getAgeLimit().equals(AGE_LIMIT_18)) {
            Sid roleUser18Plus = new GrantedAuthoritySid(ROLE_USER_18);
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, roleUser18Plus, true);
            Sid roleAdmin = new GrantedAuthoritySid(ROLE_ADMIN);
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, roleAdmin, true);
        }
        else {
            // Обычная книга
            Sid roleUser18Plus = new GrantedAuthoritySid(ROLE_USER_18);
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, roleUser18Plus, true);
            Sid roleUser = new GrantedAuthoritySid(ROLE_USER);
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, roleUser, true);
            Sid roleAdmin = new GrantedAuthoritySid(ROLE_ADMIN);
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, roleAdmin, true);
        }

        mutableAclService.updateAcl(acl);
    }

    private boolean is18Plus(Acl acl) {
        List<AccessControlEntry> aclEntries = acl.getEntries();
        for (AccessControlEntry entry: aclEntries) {
            Sid sid = entry.getSid();
            if(sid instanceof GrantedAuthoritySid) {
                GrantedAuthoritySid gSid = (GrantedAuthoritySid)sid;
                if (gSid.getGrantedAuthority().equals(ROLE_USER_18)) return true;
            }
        }
        return false;
    }

    public void createCommentAcl(Comment comment) {

        // Retrieve the current principal, in order to know who "owns" this ACL
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PrincipalSid ownerSid = new PrincipalSid(auth);

        // Create the acl_object_identity row
        final ObjectIdentity oid = new ObjectIdentityImpl(Comment.class, comment.getId());

        MutableAcl acl = mutableAclService.createAcl(oid);

        // Назначим права владельцу. Владельцу все права
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, ownerSid, true);

        // Проверяем, является ли книга к которой делается комментарий 18+ или нет
        Acl parentBookAcl = mutableAclService.readAclById(new ObjectIdentityImpl(Book.class, comment. getBookId()));
        if ( is18Plus(parentBookAcl) ) {
            // комментарий к книге 18+, не даем польномочий пользователю ROLE_USER
            // User01 не сможет посмотреть комментарии по URL: http://localhost:8080/books/3/comments
            // Примечание, книга id = 3 является 18+
            Sid roleUser18Plus = new GrantedAuthoritySid(ROLE_USER_18);
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, roleUser18Plus, true);
            Sid roleAdmin = new GrantedAuthoritySid(ROLE_ADMIN);
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, roleAdmin, true);
        }
        else {
            // Обычная книга
            Sid roleUser18Plus = new GrantedAuthoritySid(ROLE_USER_18);
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, roleUser18Plus, true);
            Sid roleUser = new GrantedAuthoritySid(ROLE_USER);
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, roleUser, true);
        }

        mutableAclService.updateAcl(acl);
    }

    public void deleteBookAcl( long bookId ) {

        // Create the acl_object_identity row
        final ObjectIdentity oid = new ObjectIdentityImpl(Book.class, bookId);

        // true - удалит ACL дочерних объектов
        mutableAclService.deleteAcl(oid, true);
    }

    public void deleteBookCommentAcl( long commentId ) {

        // Create the acl_object_identity row
        final ObjectIdentity oid = new ObjectIdentityImpl(Comment.class, commentId);

        // true - удалит ACL дочерних объектов, false - нет
        mutableAclService.deleteAcl(oid, false);
    }
}
