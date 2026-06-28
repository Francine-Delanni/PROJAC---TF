package com.af.Dominio.Dados;

import com.af.Dominio.Entidades.Receita;

public interface ReceitasRepository {
    Receita recuperaReceita(long id);
    
}

