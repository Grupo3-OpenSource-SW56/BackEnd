package com.fast.learners.platform.iam.interfaces.acl;

import com.fast.learners.platform.iam.domain.model.commands.SignUpCommand;
import com.fast.learners.platform.iam.domain.model.entities.Membership;
import com.fast.learners.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.fast.learners.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.fast.learners.platform.iam.domain.services.UserCommandService;
import com.fast.learners.platform.iam.domain.services.UserQueryService;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * IamContextFacade
 * <p>
 *     This class is a facade for the IAM context. It provides a simple interface for other bounded contexts to interact with the
 *     IAM context.
 *     This class is a part of the ACL layer.
 * </p>
 *
 */
public class IamContextFacade {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public IamContextFacade(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    /**
     * Creates a user with the given email and password.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return The id of the created user.
     */
    public Long createUser(String email, String password) {
        var signUpCommand = new SignUpCommand(email, password, List.of(Membership.getDefaultMembership()));
        var result = userCommandService.handle(signUpCommand);
        if (result.isEmpty()) return 0L;
        return result.get().getId();
    }

    /**
     * Creates a user with the given email, password and memberships.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param roleNames The names of the memberships of the user. When a membership does not exist, it is ignored.
     * @return The id of the created user.
     */
    public Long createUser(String email, String password, List<String> roleNames) {
        var memberships = roleNames != null ? roleNames.stream().map(Membership::toMembershipFromName).toList() : new ArrayList<Membership>();
        var signUpCommand = new SignUpCommand(email, password, memberships);
        var result = userCommandService.handle(signUpCommand);
        if (result.isEmpty()) return 0L;
        return result.get().getId();
    }

    /**
     * Fetches the id of the user with the given email.
     * @param email The email of the user.
     * @return The id of the user.
     */
    public Long fetchUserIdByEmail(String email) {
        var getUserByemailQuery = new GetUserByEmailQuery(email);
        var result = userQueryService.handle(getUserByemailQuery);
        if (result.isEmpty()) return 0L;
        return result.get().getId();
    }

    /**
     * Fetches the email of the user with the given id.
     * @param userId The id of the user.
     * @return The email of the user.
     */
    public String fetchEmailByUserId(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var result = userQueryService.handle(getUserByIdQuery);
        if (result.isEmpty()) return Strings.EMPTY;
        return result.get().getEmail();
    }

}