package com.chatmulticanale.exception;

import com.chatmulticanale.view.navigation.Navigazione;

/**
 * Un'eccezione speciale usata per interrompere il flusso di ingresso
 * quando l'utente inserisce un comando di navigazione (es. "/b" per indietro).
 * Non rappresenta un errore, ma un evento di controllo del flusso.
 */
public class CommandException extends RuntimeException {
  private final Navigazione navigazione;

  public CommandException(Navigazione navigazione) {
    this.navigazione = navigazione;
  }

  public Navigazione getNavigazione() {
    return navigazione;
  }
}
