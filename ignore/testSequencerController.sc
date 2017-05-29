
(
  API.mountDuplexOSC();

  s.quit;
  s.options.inDevice = "JackRouter";
  s.options.outDevice = "JackRouter";
  s.boot();
  s.doWhenBooted({

    var sequencerController,
      store = StateStore.getInstance();

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
          playingState: "STOPPED"
        )
      )
    ));

    "Queueing sequencer..".postln();
    {
      2.0.wait();
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
            playingState: "QUEUED"
          )
        )
      ));
    }.fork();


    sequencerController = SequencerController.new((
      store: store
    ));

  });
)
