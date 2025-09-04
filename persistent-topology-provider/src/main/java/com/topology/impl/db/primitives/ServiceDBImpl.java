package com.topology.impl.db.primitives;

import com.topology.primitives.Service;

import jakarta.persistence.*;

@Entity
@Table(name = "service")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ServiceDBImpl extends TopologyElementDBImpl implements Service{
}
