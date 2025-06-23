package com.chatmulticanale.utils;

import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.model.Progetto;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.dto.ProgettoResponsabileDTO;
import com.chatmulticanale.dto.ChatPrivataDTO;

import java.util.List;

/**
 * Classe helper per la gestione degli input da console.
 * Fornisce metodi riutilizzabili per validare e interpretare i comandi degli utenti,
 * inclusa la selezione di ID validi da liste di entità.
 * Rileva comandi di navigazione (es. "/b", "/back") tramite l'eccezione {@link CommandException}.
 */
public final class InputHelper {

    // Costruttore privato per evitare istanziazione
    private InputHelper() {
    }

    /**
     * Chiede all'utente di inserire un ID valido da una lista di entità.
     * <p>
     * Gestisce la conversione, la validazione contro la lista fornita e i comandi di navigazione.
     * Se l'input non è numerico o non corrisponde a un ID presente nella lista,
     * mostra un messaggio di errore e richiede nuovamente l'input.
     *
     * @param prompt             messaggio da visualizzare per richiedere l'ID
     * @param listaPerControllo  lista di entità con cui verificare l'esistenza dell'ID;
     *                           i tipi supportati sono {@link Utente}, {@link Progetto},
     *                           {@link ProgettoResponsabileDTO}, {@link CanaleProgetto} e {@link ChatPrivataDTO}
     * @return ID valido inserito dall'utente
     * @throws IllegalStateException se la lista di controllo è null o vuota (errore di configurazione)
     * @throws CommandException     se l'utente inserisce un comando di navigazione (es. "/b", "/back")
     */
    public static int chiediIdValido(String prompt, List<?> listaPerControllo) throws CommandException {
        if (listaPerControllo == null || listaPerControllo.isEmpty()) {
            throw new IllegalStateException("La lista per la validazione non può essere né null né vuota.");
        }

        while (true) {
            String idStr = InputUtils.askForInput(prompt);

            try {
                final int id = Integer.parseInt(idStr);
                boolean idTrovato = false;

                for (Object oggetto : listaPerControllo) {
                    if (oggetto instanceof Utente && ((Utente) oggetto).getIdUtente() == id
                            || oggetto instanceof Progetto && ((Progetto) oggetto).getIdProgetto() == id
                            || oggetto instanceof ProgettoResponsabileDTO && ((ProgettoResponsabileDTO) oggetto).getIdProgetto() == id
                            || oggetto instanceof CanaleProgetto && ((CanaleProgetto) oggetto).getIdCanale() == id
                            || oggetto instanceof ChatPrivataDTO && ((ChatPrivataDTO) oggetto).getIdChat() == id) {
                        idTrovato = true;
                        break;
                    }
                }

                if (idTrovato) {
                    return id;
                } else {
                    ViewUtils.println(ColorUtils.ANSI_RED +
                            "ERRORE: L'ID inserito non è presente nella lista. Riprova." +
                            ColorUtils.ANSI_RESET);
                }
            } catch (NumberFormatException e) {
                ViewUtils.println(ColorUtils.ANSI_RED +
                        "ERRORE: Inserisci un ID numerico valido." +
                        ColorUtils.ANSI_RESET);
            }
        }
    }
}