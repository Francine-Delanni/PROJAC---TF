package com.af.Dominio.Dados;

import java.util.Optional;

public interface AutenticacaoRepository {
    void save(String email, String senhaHash, String cpf);
    Optional<String> findSenhaHashByEmail(String email);
}
