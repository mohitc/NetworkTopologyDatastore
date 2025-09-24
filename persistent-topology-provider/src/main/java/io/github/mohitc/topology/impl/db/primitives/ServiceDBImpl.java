package io.github.mohitc.topology.impl.db.primitives;

import io.github.mohitc.topology.primitives.Service;

import jakarta.persistence.*;

@Entity
@Table(name = "service")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ServiceDBImpl extends TopologyElementDBImpl implements Service{
}
