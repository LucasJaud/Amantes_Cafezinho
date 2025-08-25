package br.edu.ifpb.academico.Amantes_Cafezinho.dtos;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Role;

import java.util.List;

public record UpdateRolesUser(Long userId, List<Long> rolesIds) { }
