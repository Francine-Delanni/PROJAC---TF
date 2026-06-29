package com.af.estoque.repositorio;

import com.af.estoque.dominio.EstoqueProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<EstoqueProduto, Long> {
}
