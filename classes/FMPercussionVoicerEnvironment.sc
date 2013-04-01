FMPercussionVoicerEnvironment : VoicerEnvironmentComponent {
  
  init {
    arg params;

    params['numVoices'] = 8;
    params['instr'] = Instr("cs.fm.OrganicPercussion");
    params['inChannel'] = 4;

    this.outputBus = 8;

    super.init(params);

    this.voicer.mapGlobal(\amp, value: 1.0);
    this.sock.addControl(7, \amp);

  }

}
