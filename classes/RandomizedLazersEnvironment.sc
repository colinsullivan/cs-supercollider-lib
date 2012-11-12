RandomizedLazersEnvironment : VoicerEnvironmentComponent {

  init {
    arg params;
    
    params['numVoices'] = 4;
    params['instr'] = Instr("fm.Lazers");
    params['inChannel'] = 2;

    super.init(params);

    this.sock.lowkey = "E4".notemidi();
    this.sock.hikey = "E4".notemidi();

    this.sock.noteOnArgsPat = Pbind(
      \modIndex,  Pfunc({ exprand(0.01, 6); }),
      \mod2Index,  Pfunc({ exprand(0.02, 6); })
    );

  }
}
