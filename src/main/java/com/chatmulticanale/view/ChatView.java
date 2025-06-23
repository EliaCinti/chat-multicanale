package com.chatmulticanale.view;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.TipoContestoChat;
import com.chatmulticanale.utils.ColorUtils;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.costanti_view.CostantiView;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Vista per la gestione della conversazione in un canale di progetto o in una chat privata.
 * Mostra i messaggi paginati, supporta l'invio di messaggi semplici, con citazione,
 * e, se in contesto di canale, l'avvio di nuove chat private.
 * <p>
 * Utilizza {@link InterazioneUtenteController} per recuperare e inviare messaggi,
 * e gestisce comandi di navigazione (/b per tornare indietro).
 */
public class ChatView {

    /** Controller per l'invio e il recupero dei messaggi. */
    private final InterazioneUtenteController controller;

    /** ID del contesto (canale o chat privata). */
    private final int idContesto;

    /** Tipo di contesto della chat (canale o chat privata). */
    private final TipoContestoChat tipoContesto;

    /** Modalità sola lettura: disabilita l'invio di nuovi messaggi. */
    private final boolean solaLettura;

    /** ID dell'utente corrente (mittente dei messaggi). */
    private final int idUtenteLoggato;

    /** Formato per la visualizzazione delle date nei messaggi. */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(CostantiView.FORMATO_DATA);

    /**
     * Costruisce la vista per la chat specificata.
     *
     * @param controller      controller per gestire operazioni di interazione utente
     * @param idContesto      identificativo del canale o chat privata
     * @param tipoContesto    contesto della chat (CANALE_PROGETTO o CHAT_PRIVATA)
     * @param solaLettura     se true, disabilita invio di nuovi messaggi
     * @param idUtenteLoggato identificativo dell'utente che visualizza/invia
     */
    public ChatView(InterazioneUtenteController controller, int idContesto, TipoContestoChat tipoContesto, boolean solaLettura, int idUtenteLoggato) {
        this.controller = controller;
        this.idContesto = idContesto;
        this.tipoContesto = tipoContesto;
        this.solaLettura = solaLettura;
        this.idUtenteLoggato = idUtenteLoggato;
    }

    /**
     * Avvia il ciclo di visualizzazione e interazione della chat.
     * Permette la navigazione tra le pagine di messaggi, l'invio di nuovi messaggi,
     * l'inserimento di citazioni e l'avvio di chat private (se in canale).
     * Gestisce i comandi di ritorno (/b) per uscire dalla vista.
     */
    public void show() {
        int paginaCorrente = 1;
        while (true) {
            ViewUtils.clearScreen();
            List<MessaggioDTO> messaggi = (tipoContesto == TipoContestoChat.CANALE_PROGETTO)
                    ? controller.getPaginaMessaggiCanale(idContesto, idUtenteLoggato, paginaCorrente)
                    : controller.getPaginaMessaggiChatPrivata(idContesto, idUtenteLoggato, paginaCorrente);

            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- Conversazione (Pagina " + paginaCorrente + ") ---" + ColorUtils.ANSI_RESET);
            if (solaLettura) {
                ViewUtils.println(ColorUtils.ANSI_YELLOW + CostantiView.MODALITA_LETTURA + ColorUtils.ANSI_RESET);
            }
            ViewUtils.printSeparator();

            if (messaggi.isEmpty()) {
                if (paginaCorrente > 1) {
                    ViewUtils.println(CostantiView.NO_ALTRE_PAGINE);
                    paginaCorrente--;
                } else {
                    ViewUtils.println(CostantiView.NO_MESSAGGIO_IN_CONVERSAZIONE);
                }
            } else {
                for (MessaggioDTO msg : messaggi) {
                    String riga = String.format("ID %-4d | [%s] %s%s%s: %s",
                            msg.getIdMessaggio(),
                            dateFormat.format(msg.getTimestamp()),
                            ColorUtils.ANSI_BOLD,
                            msg.getUsernameMittente(),
                            ColorUtils.ANSI_RESET,
                            msg.getContenuto());
                    ViewUtils.println(riga);
                    if (msg.getIdMessaggioCitato() != null) {
                        String citazione = String.format("    -> cita messaggio [ID %d]", msg.getIdMessaggioCitato());
                        ViewUtils.println(ColorUtils.ANSI_CYAN + citazione + ColorUtils.ANSI_RESET);
                    }
                }
            }

            ViewUtils.printSeparator();
            ViewUtils.print("Opzioni: [N] Pag. Succ. | [P] Pag. Prec.");
            if (!solaLettura) {
                ViewUtils.print(" | [I] Invia | [C] Cita");
                if (tipoContesto == TipoContestoChat.CANALE_PROGETTO) {
                    ViewUtils.print(" | [A] Avvia Chat Privata");
                }
            }
            ViewUtils.println(" | [/b] Indietro");

            try {
                String scelta = InputUtils.askForInput("Comando: ").toLowerCase();
                switch (scelta) {
                    case "n" -> paginaCorrente++;
                    case "p" -> paginaCorrente = Math.max(1, paginaCorrente - 1);
                    case "i" -> { if (!solaLettura) handleInviaMessaggio(null); }
                    case "c" -> { if (!solaLettura) handleCitaMessaggio(messaggi); }
                    case "a" -> { if (!solaLettura && tipoContesto == TipoContestoChat.CANALE_PROGETTO) handleAvviaChatPrivata(messaggi); }
                    default -> {
                        ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.COMANDO_NON_RICONOSCIUTO + ColorUtils.ANSI_RESET);
                        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_RIPROVARE);
                    }
                }
            } catch (CommandException e) {
                return; // Torna indietro alla vista chiamante
            }
        }
    }

    /**
     * Invia un nuovo messaggio, con eventuale citazione.
     *
     * @param idMessaggioCitato ID del messaggio da citare (null per invio semplice)
     * @throws CommandException se l'utente invia un comando di navigazione
     */
    private void handleInviaMessaggio(Integer idMessaggioCitato) throws CommandException {
        ViewUtils.printSeparator();
        String prompt = (idMessaggioCitato == null)
                ? "Scrivi il tuo messaggio (o /b per annullare): "
                : "Scrivi la tua risposta al messaggio " + idMessaggioCitato + " (o /b per annullare): ";
        String contenuto = InputUtils.askForInput(prompt);
        boolean successo;
        if (tipoContesto == TipoContestoChat.CANALE_PROGETTO) {
            successo = (idMessaggioCitato == null)
                    ? controller.inviaMessaggioCanale(idContesto, idUtenteLoggato, contenuto)
                    : controller.inviaMessaggioConCitazioneCanale(idContesto, idUtenteLoggato, contenuto, idMessaggioCitato);
        } else {
            successo = (idMessaggioCitato == null)
                    ? controller.inviaMessaggioChat(idContesto, idUtenteLoggato, contenuto)
                    : controller.inviaMessaggioConCitazioneChat(idContesto, idUtenteLoggato, contenuto, idMessaggioCitato);
        }
        ViewUtils.println(successo
                ? ColorUtils.ANSI_GREEN + CostantiView.MESSAGGIO_INVIATO + ColorUtils.ANSI_RESET
                : ColorUtils.ANSI_RED + CostantiView.ERRORE_INVIO_MESSAGGIO + ColorUtils.ANSI_RESET);
        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_CONTINUARE);
    }


    /**
     * Seleziona e invia una citazione di un messaggio esistente.
     *
     * @param messaggiVisibili lista dei messaggi attualmente visibili
     */
    private void handleCitaMessaggio(List<MessaggioDTO> messaggiVisibili) {
        if (messaggiVisibili.isEmpty()) {
            ViewUtils.println(ColorUtils.ANSI_YELLOW + CostantiView.NO_MESSAGGI_IN_PAGINA_DA_CITARE + ColorUtils.ANSI_RESET);
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
            return;
        }
        try {
            int idDaCitare = InputUtils.readInt("Inserisci l'ID del messaggio da citare: ");
            boolean idValido = messaggiVisibili.stream().anyMatch(m -> m.getIdMessaggio() == idDaCitare);
            if (idValido) {
                handleInviaMessaggio(idDaCitare);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.ID_NON_VALIDO_PER_CITAZIONE + ColorUtils.ANSI_RESET);
                InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_RIPROVARE);
            }
        } catch (CommandException e) {
            ViewUtils.println(CostantiView.CITAZIONE_ANNULLATA);
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_CONTINUARE);
        }
    }


    /**
     * Avvia una nuova chat privata a partire da un messaggio in un canale.
     *
     * @param messaggiVisibili lista dei messaggi attualmente visibili
     */
    private void handleAvviaChatPrivata(List<MessaggioDTO> messaggiVisibili) {
        if (messaggiVisibili.isEmpty()) {
            ViewUtils.println(ColorUtils.ANSI_YELLOW + CostantiView.NO_MESSAGGI_DA_CUI_PARTIRE + ColorUtils.ANSI_RESET);
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
            return;
        }
        try {
            int idMessaggioOrigine = InputUtils.readInt("Inserisci l'ID del messaggio da cui avviare la chat privata: ");
            boolean idValido = messaggiVisibili.stream().anyMatch(m -> m.getIdMessaggio() == idMessaggioOrigine);
            if (idValido) {
                java.util.Optional<String> errorOptional = controller.avviaNuovaChatPrivata(idMessaggioOrigine, idUtenteLoggato);
                if (errorOptional.isEmpty()) {
                    ViewUtils.println(ColorUtils.ANSI_GREEN + CostantiView.CHAT_AVVIATA + ColorUtils.ANSI_RESET);
                    ViewUtils.println("Ora puoi accedervi dalla sezione 'Chat Private' nella home.");
                } else {
                    ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.ERRORE_AVVIO_CHAT + ColorUtils.ANSI_RESET);
                    ViewUtils.println(ColorUtils.ANSI_RED + "Motivo: " + errorOptional.get() + ColorUtils.ANSI_RESET);
                }
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.ID_NON_VALIDO_PER_AVVIARE_CHAT + ColorUtils.ANSI_RESET);
            }
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_CONTINUARE);
        } catch (CommandException e) {
            ViewUtils.println(CostantiView.OPERAZIONE_ANNULLATA);
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
        }
    }
}