package com.appsmith.server.solutions;

import com.appsmith.server.repositories.CommentRepository;
import com.appsmith.server.repositories.NotificationRepository;
import com.appsmith.server.repositories.WorkspaceRepository;
import com.appsmith.server.solutions.ce.UserChangedHandlerCEImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserChangedHandlerImpl extends UserChangedHandlerCEImpl implements UserChangedHandler {

    public UserChangedHandlerImpl(ApplicationEventPublisher applicationEventPublisher,
                                  CommentRepository commentRepository,
                                  NotificationRepository notificationRepository,
                                  WorkspaceRepository organizationRepository) {

        super(applicationEventPublisher, commentRepository, notificationRepository, organizationRepository);
    }
}
