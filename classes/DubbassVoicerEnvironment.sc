DubbassVoicerEnvironment : VoicerEnvironmentComponent {
  
  init {
    arg params;

    var voicer,
      sock,
      dubBass = Instr("cs.synths.DubBass"),
      dubBassSpecs = dubBass.specs,
      gui;

    params['numVoices'] = 1;
    params['instr'] = dubBass;
    params['inChannel'] = 3;

    this.outputBus = 4;

    super.init(params);

    this.voicer.mapGlobal(\amp);
    this.voicer.mapGlobal(
      \rateMultiplier,
      spec: dubBassSpecs.at(dubBass.argsAndIndices().at(\rateMultiplier))
    );

    this.voicer.portaTime = 0;

    this.sock.addControl(7, \amp);
    this.sock.addControl(15, \rateMultiplier);

  }

}
