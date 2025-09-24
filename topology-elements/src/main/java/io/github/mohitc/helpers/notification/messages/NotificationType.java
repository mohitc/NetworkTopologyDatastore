package io.github.mohitc.helpers.notification.messages;

import java.util.*;

public enum NotificationType {
  TENotification(new NotificationType[]{}),
  TECreationNotification(new NotificationType[]{TENotification}),
  TEDeletionNotification(new NotificationType[]{TENotification}),
  TEModificationNotification(new NotificationType[]{TENotification}),
  NENotification(new NotificationType[]{TENotification}),
  NECreationNotification(new NotificationType[]{NENotification, TECreationNotification}),
  NEDeletionNotification(new NotificationType[]{NENotification, TEDeletionNotification}),
  NEModificationNotification(new NotificationType[]{NENotification, TEModificationNotification}),
  CPNotification(new NotificationType[]{TENotification}),
  CPCreationNotification(new NotificationType[]{CPNotification, TECreationNotification}),
  CPDeletionNotification(new NotificationType[]{CPNotification, TEDeletionNotification}),
  CPModificationNotification(new NotificationType[]{CPNotification, TEModificationNotification}),
  PortNotification(new NotificationType[]{CPNotification}),
  PortCreationNotification(new NotificationType[]{PortNotification, CPCreationNotification}),
  PortDeletionNotification(new NotificationType[]{PortNotification, CPDeletionNotification}),
  PortModificationNotification(new NotificationType[]{PortNotification, CPModificationNotification}),
  ConnNotification(new NotificationType[]{TENotification}),
  ConnCreationNotification(new NotificationType[]{ConnNotification, TECreationNotification}),
  ConnDeletionNotification(new NotificationType[]{ConnNotification, TEDeletionNotification}),
  ConnModificationNotification(new NotificationType[]{ConnNotification, TEModificationNotification}),
  LinkNotification(new NotificationType[]{ConnNotification}),
  LinkCreationNotification(new NotificationType[]{LinkNotification, ConnCreationNotification}),
  LinkDeletionNotification(new NotificationType[]{LinkNotification, ConnDeletionNotification}),
  LinkModificationNotification(new NotificationType[]{LinkNotification, ConnModificationNotification}),
  CrsCcNotification(new NotificationType[]{ConnNotification}),
  CrsCcCreationNotification(new NotificationType[]{CrsCcNotification, ConnCreationNotification}),
  CrsCcDeletionNotification(new NotificationType[]{CrsCcNotification, ConnDeletionNotification}),
  CrsCcModificationNotification(new NotificationType[]{CrsCcNotification, ConnModificationNotification});

  private final NotificationType[] parents;

  NotificationType(NotificationType[] parent) {
    this.parents = parent;
  }

  public String getStrFormat() {
    return this + allParents.get(this).toString();
  }

  public Set<NotificationType> getAllParents() {
    return allParents.get(this);
  }



  private static final Map<NotificationType, Set<NotificationType>> allParents;
  static {
    allParents = new HashMap<>();
    //values arrive in hierarchical order
    Arrays.stream(values()).forEach(currentChild -> {
      Set<NotificationType> parents = new HashSet<>();
      Arrays.stream(currentChild.parents).forEach(parent -> {
        parents.add(parent);
        parents.addAll(allParents.get(parent));
      });
      allParents.put(currentChild, Collections.unmodifiableSet(parents));
    });
  }

}
