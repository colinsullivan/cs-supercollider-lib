RhodesVoicerEnvironment : VoicerEnvironmentComponent {
  init {
    arg params;

    params['numVoices'] = 8;
    params['instr'] = Instr("fm.Rhodes");
    params['inChannel'] = 1;

    super.init(params);

    this.sock.addControl(7, \amp);
  }
}
