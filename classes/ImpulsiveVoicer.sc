ImpulsiveVoicer : VoicerEnvironmentComponent {
  init {
    arg params;

    var instr = Instr("cs.percussion.Impulsive"),
      specs = instr.specs;

    params['numVoices'] = 3;
    params['instr'] = instr;

    super.init(params);
  }
}
