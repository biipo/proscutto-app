# D6 - Versione 1.0 del codice sorgente

## Gruppo: Proscutto

- Capo gruppo : Filippo Zangrossi (896101)
- Altri membri:
    - Giulio Furlan (896140)
    - Giovanni Marola (887931)

## Stato di avanzamento dello sviluppo

È stata implementata la struttura di base dell'applicazione, con le tab principali e in ciascuna
sono state implementate le funzionalità di base.

### Stato avanzamento singole parti

- 4 tab principali
    - **home**: implementata la UI di base con un esempio.\
            *Prossimo passo*: sincronizzazione con i dati degli utenti presenti su firebase
    - **impostazioni**: implementato task *logout*\
            *Prossimo passo*: aggiungere altre impostazioni e reindirizzare l'utente alla 
            schermata di *login* quando viene fatto il *logout*
    - **task**: sincronizzazione delle task con database firebase
    - **quiz**: Invece di memorizzare i risultati dei quiz in locale, si propone di implementare un sistema che colleghi tali risultati direttamente all'account dell'utente. Questo approccio non solo garantirebbe una maggiore sicurezza e integrità dei dati, ma permetterebbe anche di monitorare e visualizzare i progressi del personaggio dell'utente in modo più dettagliato e interattivo.

    Attraverso l'integrazione con Firebase, ogni volta che un utente completa un quiz, i risultati verrebbero automaticamente inviati e archiviati nel database cloud, creando così un registro persistente e accessibile da qualsiasi dispositivo. Questo sistema consentirebbe di tracciare l'evoluzione delle competenze e delle performance dell'utente nel tempo, offrendo la possibilità di visualizzare statistiche e grafici che rappresentano i progressi compiuti. Inoltre, l'associazione dei risultati all'account personale dell'utente favorirebbe un'esperienza di apprendimento più personalizzata, in cui i feedback e le ricompense potrebbero essere adattati in base alle performance individuali, rendendo il percorso di apprendimento più coinvolgente e motivante.
- **pagina negozio**: *ancora da implementare*
- **pagina social**: *ancora da implementare*
- **pagine di autenticazione**: terminata implementazione *login* e *signup*\
    *Prossimo passo*: implementazione del sistema *password dimenticata*
