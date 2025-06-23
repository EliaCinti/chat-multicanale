package com.chatmulticanale.view;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.dto.ChatPrivataDTO;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.model.TipoContestoChat;
import com.chatmulticanale.utils.*;
import com.chatmulticanale.view.costanti_view.CostantiView;
import com.chatmulticanale.view.navigation.Navigazione;
import java.util.List;

/**
 * Helper riutilizzabile per gestire le funzionalità di comunicazione comuni
 * a più viste dell'applicazione.
 * <p>
 * Fornisce metodi per accedere ai canali di progetto, visualizzare chat private,
 * e avviare nuove conversazioni, delegando l'interazione al controller.
 */
public class ComunicazioneViewHelper {

    /**
     * Controller per gestire l'invio e il recupero dei messaggi.
     */
    private final InterazioneUtenteController interazioneController;

    /**
     * Costruisce il helper per la comunicazione.
     *
     * @param interazioneController controller da utilizzare per tutte le operazioni di messaggistica
     */
    public ComunicazioneViewHelper(InterazioneUtenteController interazioneController) {
        this.interazioneController = interazioneController;
    }

    /**
     * Mostra e permette all'utente di selezionare un canale di progetto
     * a cui partecipa per inviare o visualizzare messaggi.
     *
     * @param idUtenteLoggato ID dell'utente autenticato
     * @throws CommandException se l'utente digita un comando di navigazione (es. /b)
     */
    public void handleAccessoCanaliProgetto(int idUtenteLoggato) throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.ACCESSO_AI_CANALI_DI_PROGETTO + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK_2);
        ViewUtils.printSeparator();

        List<CanaleProgetto> mieiCanali = interazioneController.getCanaliUtente(idUtenteLoggato);
        if (mieiCanali.isEmpty()) {
            ViewUtils.println(CostantiView.NO_PARTECIPAZIONE_A_CANALI);
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
            return;
        }

        int idCanaleSelezionato = selezionaCanale(mieiCanali, CostantiView.SELEZIONA_CANALE_PER_MESSAGGI);
        new ChatView(interazioneController,
                idCanaleSelezionato,
                TipoContestoChat.CANALE_PROGETTO,
                false,
                idUtenteLoggato).show();
    }

    /**
     * Gestisce l'accesso all'area delle chat private.
     * Permette di scegliere fra visualizzare chat correnti o avviarne una nuova.
     *
     * @param idUtenteLoggato ID dell'utente autenticato
     * @throws CommandException se l'utente digita un comando di navigazione
     */
    public void handleAccessoChatPrivate(int idUtenteLoggato) throws CommandException {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.AREA_CHAT_PRIVATE + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.B_O_BACK_2);
            ViewUtils.printSeparator();
            ViewUtils.println(CostantiView.ACCEDI_CHAT_PRIVATE_OPZIONE_1);
            ViewUtils.println(CostantiView.ACCEDI_CHAT_PRIVATE_OPZIONE_2);
            ViewUtils.printSeparator();

            int scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 1, 2);
            try {
                switch (scelta) {
                    case 1 -> handleVisualizzaMieChat(idUtenteLoggato);
                    case 2 -> handleAvviaNuovaChatDaCanale(idUtenteLoggato);
                }
            } catch (CommandException e) {
                return;
            }
        }
    }

    /**
     * Visualizza l'elenco delle chat private dell'utente e permette di selezionarne una.
     *
     * @param idUtenteLoggato ID dell'utente autenticato
     * @throws CommandException se l'utente digita un comando di navigazione
     */
    public void handleVisualizzaMieChat(int idUtenteLoggato) throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.LE_TUE_CHAT_PRIVATE + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK_2);
        ViewUtils.printSeparator();

        List<ChatPrivataDTO> mieChat = interazioneController.getMieChatPrivate(idUtenteLoggato);
        if (mieChat.isEmpty()) {
            ViewUtils.println(CostantiView.NO_CHAT_ATTIVE);
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
            return;
        }

        ViewUtils.println(CostantiView.ELENCO_CHAT_PRIVATE);
        String header = String.format("%-5s | %-20s | %s", "ID", "Avviata il", "Conversazione con");
        ViewUtils.println(ColorUtils.ANSI_BOLD + header + ColorUtils.ANSI_RESET);
        mieChat.forEach(chat -> {
            String riga = String.format("%-5d | %-20s | %s",
                    chat.getIdChat(), chat.getDataCreazione(), chat.getAltroPartecipanteUsername());
            ViewUtils.println(riga);
        });
        ViewUtils.printSeparator();

        int idChatSelezionata = InputHelper.chiediIdValido(CostantiView.VISUALIZZA_CHAT_INSERISCI_ID_CHAT, mieChat);
        new ChatView(interazioneController,
                idChatSelezionata,
                TipoContestoChat.CHAT_PRIVATA,
                false,
                idUtenteLoggato).show();
    }

    /**
     * Avvia una nuova chat privata a partire da un canale esistente.
     *
     * @param idUtenteLoggato ID dell'utente autenticato
     * @throws CommandException se l'utente digita un comando di navigazione
     */
    public void handleAvviaNuovaChatDaCanale(int idUtenteLoggato) throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.AVVIA_NUOVA_CHAT_PRIVATA + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK_2);
        ViewUtils.printSeparator();

        List<CanaleProgetto> mieiCanali = interazioneController.getCanaliUtente(idUtenteLoggato);
        if (mieiCanali.isEmpty()) {
            ViewUtils.println(CostantiView.AVVIA_NUOVA_CHAT_NO_CANALI);
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
            return;
        }

        int idCanaleSelezionato = selezionaCanale(mieiCanali, CostantiView.AVVIA_NUOVA_CHAT_SELEZIONA_CANALE);
        int idMessaggioSelezionato = selezionaMessaggioDaCanale(idCanaleSelezionato, idUtenteLoggato);

        MessaggioDTO msgPlaceholder = new MessaggioDTO();
        msgPlaceholder.setIdMessaggio(idMessaggioSelezionato);
        ViewActionHelper.avviaChatPrivataDaListaMessaggi(List.of(msgPlaceholder), idUtenteLoggato, interazioneController);

        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
    }

    /**
     * Mostra un elenco di canali e restituisce l'ID selezionato.
     *
     * @param canali lista dei canali a disposizione
     * @param prompt messaggio da mostrare per la selezione
     * @return ID del canale scelto
     * @throws CommandException se l'utente digita un comando di navigazione
     */
    private int selezionaCanale(List<CanaleProgetto> canali, String prompt) throws CommandException {
        ViewUtils.println("\n" + prompt);
        canali.forEach(c -> ViewUtils.println(String.format("  ID: %-5d | Nome:%s", c.getIdCanale(), c.getNomeCanale())));
        ViewUtils.printSeparator();
        return InputHelper.chiediIdValido("ID Canale: ", canali);
    }

    /**
     * Permette la selezione (paginata) di un messaggio all'interno di un canale.
     *
     * @param idCanale         ID del canale di origine
     * @param idUtenteLoggato  ID dell'utente autenticato
     * @return ID del messaggio scelto
     * @throws CommandException se l'utente digita un comando di navigazione o non ci sono messaggi
     */
    private int selezionaMessaggioDaCanale(int idCanale, int idUtenteLoggato) throws CommandException {
        int paginaCorrente = 1;
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- Seleziona un Messaggio (Pagina " + paginaCorrente + ") ---" + ColorUtils.ANSI_RESET);
            ViewUtils.printSeparator();

            List<MessaggioDTO> messaggi = interazioneController.getPaginaMessaggiCanale(idCanale, idUtenteLoggato, paginaCorrente);
            if (messaggi.isEmpty()) {
                if (paginaCorrente > 1) {
                    ViewUtils.println(CostantiView.NO_ALTRE_PAGINE);
                    paginaCorrente--;
                    InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_CONTINUARE);
                    continue;
                } else {
                    ViewUtils.println(CostantiView.NO_MESSAGGIO_IN_CANALE);
                    InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
                    throw new CommandException(Navigazione.indietro());
                }
            }

            messaggi.forEach(msg -> {
                String riga = String.format("ID %-4d | [%s] %s: %s",
                        msg.getIdMessaggio(),
                        new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(msg.getTimestamp()),
                        msg.getUsernameMittente(),
                        msg.getContenuto());
                ViewUtils.println(riga);
            });
            ViewUtils.printSeparator();

            String input = InputUtils.askForInput(
                            "Inserisci l'ID del messaggio o naviga ([N] Pag. Succ. | [P] Pag. Prec. | [/b] Annulla): ")
                    .toLowerCase();

            switch (input) {
                case "n" -> paginaCorrente++;
                case "p" -> {
                    if (paginaCorrente > 1) paginaCorrente--;
                    else {
                        ViewUtils.println(CostantiView.PRIMA_PAGINA);
                        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_CONTINUARE);
                    }
                }
                default -> {
                    try {
                        int idSelezionato = Integer.parseInt(input);
                        boolean idValido = messaggi.stream().anyMatch(m -> m.getIdMessaggio() == idSelezionato);
                        if (idValido) return idSelezionato;
                        ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.ID_NON_VALIDO_IN_PAGINA_CORRENTE + ColorUtils.ANSI_RESET);
                    } catch (NumberFormatException e) {
                        ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.COMANDO_NON_RICONOSCIUTO + ColorUtils.ANSI_RESET);
                    }
                    InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_RIPROVARE);
                }
            }
        }
    }
}
