RhodesVoicerEnvironment : VoicerEnvironmentComponent {
  init {
    arg params;

    params['numVoices'] = 8;
    params['instr'] = Instr("fm.Rhodes");
    params['inChannel'] = 1;

    super.init(params);

  }

  init_external_controller_mappings {

    super.init_external_controller_mappings();
    
    this.sock.addControl(7, \amp);

    this.uc33Controller.mapCCS(1, 'sl5', {
      arg ccval;

      "ccval:".postln;
      ccval.postln;

    });

  }
}
