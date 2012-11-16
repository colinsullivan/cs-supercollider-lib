CircularWarpInputEnvironment : PatchEnvironment {

  var <>circularBuf,
    circularBufferDuration = 3.0,
    <>inputGroup,
    <>outputGroup,
    <>inPatch,
    <>outPatch;

  init {
    arg params;

    super.init(params);

    this.inputGroup = Group.new();
    this.outputGroup = Group.after(this.inputGroup);
    
  }
  
  load_samples {
    arg callback;

    var s = Server.default;

    // need to create circular buffer
    this.circularBuf = Buffer.alloc(
      s,
      s.sampleRate * circularBufferDuration,
      1
    );

    {callback.value();}.defer(1);

  }

  /**
   *  we need two patches, one for the circular input and one to affect it.
   **/
  load_patch {
    var outInstr;

    this.inPatch = Patch(Instr.at("fx.Circular.Recorder"), (
      buf: this.circularBuf
    ));
    
    // make compound instrument
    outInstr = Instr.at("fx.Circular.Player") <>> Instr.at("fx.FFT.Shuffle");

    this.outPatch = Patch(outInstr, (
      buf: this.circularBuf,
      gate: 1
    ));

    this.inPatch.prepareForPlay();
    this.outPatch.prepareForPlay();

  }

  init_gui {
    arg params;

    var layout = params['layout'];

    super.init_gui(params);

    this.outPatch.amp.gui(layout);
  }

  on_play {
    this.inPatch.play(
      bus: Bus.audio(Server.default, this.outputBus),
      group: inputGroup
    );
    this.outPatch.play(
      bus: Bus.audio(Server.default, this.outputBus),
      group: outputGroup
    );
  }

  load_external_controller_mappings {
    super.load_external_controller_mappings();

    if (this.uc33Controller != nil, {
      this.map_uc33_to_patch('sl3', 'amp', this.outPatch);
    });

  }
}
