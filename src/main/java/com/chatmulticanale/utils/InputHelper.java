package com.chatmulticanale.utils;

import com.chatmulticanale.dto.ChatPrivataDTO;
import com.chatmulticanale.dto.ProgettoResponsabileDTO;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.model.Progetto;
import com.chatmulticanale.model.Utente;

import java.util.List;

public class InputHelper {

    private InputHelper() {
        // costruttore privato
    }

    /**
     * Metodo helper generico e riutilizzabile per chiedere all'utente di selezionare un ID valido da una lista.
     * Gestisce la validazione dell'input, i comandi di navigazione e i messaggi di errore.
     *
     * @param prompt Il messaggio da mostrare all'utente.
     * @param listaPerControllo La lista di oggetti su cui controllare l'esistenza dell'ID.
     *                          Gli oggetti devono essere di tipo Utente, Progetto, ProgettoResponsabileDTO,
     *                          CanaleProgetto o ChatPrivataDTO.
     * @return L'ID valido inserito dall'utente.
     * @throws CommandException se l'utente inserisce un comando di navigazione (es. /b, /back).
     */
    public static int chiediIdValido(String prompt, List<?> listaPerControllo) throws CommandException {
        if (listaPerControllo == null || listaPerControllo.isEmpty()) {
            // Questo è un errore di programmazione, non di input utente.
            throw new IllegalStateException("La lista per la validazione non può essere né null né vuota.");
        }

        while (true) {
            // Usiamo il nostro InputUtils per l'input grezzo e la gestione dei comandi
            String idStr = InputUtils.askForInput(prompt);

            try {
                final int id = Integer.parseInt(idStr);
                boolean idTrovato = false;

                // Controlla l'ID rispetto ai tipi di oggetto noti
                for (Object oggetto : listaPerControllo) {
                    if (oggetto instanceof Utente && ((Utente) oggetto).getIdUtente() == id) {
                        idTrovato = true;
                        break;
                    }
                    if (oggetto instanceof Progetto && ((Progetto) oggetto).getIdProgetto() == id) {
                        idTrovato = true;
                        break;
                    }
                    if (oggetto instanceof ProgettoResponsabileDTO && ((ProgettoResponsabileDTO) oggetto).getIdProgetto() == id) {
                        idTrovato = true;
                        break;
                    }
                    if (oggetto instanceof CanaleProgetto && ((CanaleProgetto) oggetto).getIdCanale() == id) {
                        idTrovato = true;
                        break;
                    }
                    if (oggetto instanceof ChatPrivataDTO && ((ChatPrivataDTO) oggetto).getIdChat() == id) {
                        idTrovato = true;
                        break;
                    }
                }

                if (idTrovato) {
                    return id; // L'ID è valido, lo restituiamo.
                } else {
                    ViewUtils.println(ColorUtils.ANSI_RED + "ERRORE: L'ID inserito non è presente nella lista. Riprova." + ColorUtils.ANSI_RESET);
                }
            } catch (NumberFormatException e) {
                ViewUtils.println(ColorUtils.ANSI_RED + "ERRORE: Inserisci un ID numerico valido." + ColorUtils.ANSI_RESET);
            }
        }
    }
}