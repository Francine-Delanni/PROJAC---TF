package com.af.Dominio.Dados;

import java.util.List;

import com.af.Dominio.Entidades.Ingrediente;

public interface IngredientesRepository {
    List<Ingrediente> recuperaTodos();
    List<Ingrediente> recuperaIngredientesReceita(long id);
}

