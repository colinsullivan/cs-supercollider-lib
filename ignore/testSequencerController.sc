(

  s.quit;
  s.boot();
  s.doWhenBooted({

    var sequencerController,
      store = StateStore.getInstance();

    ~store = store;

    store.setState((
      abletonlink: (
        bpm: 120.0,
        beat: 1.0
      ),
      sequencers: (
        metro: (
          name: "metro",
          class: "MetronomeSequencer",
          clockOffsetSeconds: 0.0,
          playQueued: false
        )
      )
    ));

    ~sequencerController = SequencerController.new((
      store: store
    ));

  });
)

(
  ~store.setState((
    abletonlink: (
      bpm: 120.0,
      beat: 1.0
    ),
    sequencers: (
      metro: (
        name: "metro",
        class: "MetronomeSequencer",
        clockOffsetSeconds: 0.0,
        playQueued: true
      )
    )
  ));
)
