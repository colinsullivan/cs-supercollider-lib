SoundDesignEnvironment {
  classvar <>instance;
  *new {
    ^super.new().init();
  }
  *start {
    arg params;
    if (this.instance != nil, {
      "SoundDesignEnvironment already started".throw();    
    }, {
      this.instance = SoundDesignEnvironment.new(params);
    });
    ^this.instance;
  }
  init {
    arg params;

    var clock,
      owaKickEnvironment,
      soundsDir = "SOUNDS_DIRECTORY_PATH".getenv(),
      bufManager,
      store = SCReduxStore.getInstance(),
      clockController = LinkClockController.new((
        store: store
      )),
      percussionKitSampleManager;

    Server.default.latency = 0.1;
    clock = LinkClock();

    bufManager = BufferManager.new((
      rootDir: soundsDir
    ));

    owaKickEnvironment = VoicerEnvironmentComponent.new((
      inChannel: 6,
      outputBus: 2,
      numVoices: 1,
      instr: Instr("cs.percussion.OWAKick"),
      lowkeyName: "C0",
      hikeyName: "C4",
      clock: clockController.clock,
      store: store,
      transpose: -12
    ));

    ^this;  
  }
}
