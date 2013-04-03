SmoothBassVoicerEnvironment : VoicerEnvironmentComponent {
  
  init {
    arg params;

    var voicer,
      sock,
      dubBass = Instr("cs.synths.SmoothAndWideBass"),
      dubBassSpecs = dubBass.specs,
      gui;

    params['numVoices'] = 1;
    params['instr'] = dubBass;
    params['inChannel'] = 3;

    this.outputBus = 12;

    super.init(params);

    this.voicer.mapGlobal(\amp, value: 1.0);
    this.voicer.mapGlobal(
      \rateMultiplier,
      spec: dubBassSpecs.at(dubBass.argsAndIndices().at(\rateMultiplier)),
      value: 2.0
    );

    this.voicer.portaTime = 0;

    this.sock.addControl(7, \amp, 1.0);
    this.sock.addControl(15, \rateMultiplier, 2.0);

  }

}
