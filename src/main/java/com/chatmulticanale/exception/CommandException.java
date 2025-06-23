package com.chatmulticanale.exception;

import com.chatmulticanale.view.navigation.Navigazione;

/**
 * Eccezione di controllo del flusso utilizzata per gestire comandi di navigazione
 * inseriti dall'utente (es. "/b" per tornare indietro).
 * <p>
 * Non rappresenta un errore di runtime, ma un segnale per modificare la logica di navigazione.
 * Contiene l'azione di navigazione desiderata.
 */
public class CommandException extends RuntimeException {

  /**
   * Tipo di navigazione da eseguire.
   */
  private final Navigazione navigazione;

  /**
   * Costruisce una nuova CommandException contenente il tipo di navigazione.
   *
   * @param navigazione oggetto {@link Navigazione} che indica l'azione di navigazione desiderata
   */
  public CommandException(Navigazione navigazione) {
    this.navigazione = navigazione;
  }

  /**
   * Restituisce il tipo di navigazione associato a questa eccezione.
   *
   * @return istanza di {@link Navigazione} che descrive l'azione di navigazione
   */
  public Navigazione getNavigazione() {
    return navigazione;
  }
}
