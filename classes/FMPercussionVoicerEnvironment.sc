FMPercussionVoicerEnvironment : VoicerEnvironmentComponent {
  
  init {
    arg params,
      instr = Instr("cs.fm.OrganicPercussion"),
      specs = instr.specs;

    params['numVoices'] = 8;
    params['instr'] = instr;
    params['inChannel'] = 4;

    super.init(params);

    this.voicer.mapGlobal(\amp, value: (1.0 / params['numVoices']), spec: \amp);
    this.sock.addControl(7, \amp);

    this.voicer.mapGlobal(
      \doFreqSweep,
      value: 0.0,
      spec: specs.at(instr.argsAndIndices().at(\doFreqSweep))
    );
    this.sock.addControl(15, \doFreqSweep);

    this.voicer.mapGlobal(
      \freqSweepTargetMultiplier,
      value: 1.0,
      spec: specs.at(instr.argsAndIndices().at(\freqSweepTargetMultiplier))
    );
    this.sock.addControl(16, \freqSweepTargetMultiplier);

    this.voicer.mapGlobal(
      \autoDurationOn,
      value: 1,
      spec: specs.at(instr.argsAndIndices().at(\autoDurationOn))
    );
    this.sock.addControl(17, \autoDurationOn);

    this.voicer.mapGlobal(
      \noteDuration,
      value: 1.0,
      spec: specs.at(instr.argsAndIndices().at(\noteDuration))
    );
    this.sock.addControl(18, \noteDuration);
  }

}
