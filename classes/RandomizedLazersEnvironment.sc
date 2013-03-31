RandomizedLazersEnvironment : VoicerEnvironmentComponent {

  init {
    arg params;
    
    params['numVoices'] = 4;
    params['instr'] = Instr("cs.fm.Lazers");
    params['inChannel'] = 2;

    this.outputBus = 2;

    super.init(params);

    this.sock.lowkey = "B4".notemidi();
    this.sock.hikey = "B4".notemidi();

    this.sock.noteOnArgsPat = Pbind(
      \modIndex,  Pfunc({ exprand(0.01, 6); }),
      \mod2Index,  Pfunc({ exprand(0.02, 6); })
    );

  }
}
