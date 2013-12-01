AbletonPannerEnvironment : PerformanceEnvironmentComponent {
  var <>decoder, <>panners;

  create_output_channel {
    ^MixerChannel.new(
      this.gui_window_title(),
      Server.default,
      2, 2,
      outbus: 0
    );
  }

  init {
    arg params;

    var panningResponder;

    /**
     *  Stereo decoders
     **/
    // Cardioids at 131 deg
    this.decoder = FoaDecoderMatrix.newStereo(131/2 * pi/180, 0.5);
    // UHJ (kernel)
    //decoder = FoaDecoderKernel.newUHJ();
    // synthetic binaural (kernel)
    //decoder = FoaDecoderKernel.newSpherical();
    // KEMAR binaural (kernel)
    //decoder = FoaDecoderKernel.newCIPIC();

    /**
     *  2D decoders
     **/
    //decoder = FoaDecoderMatrix.newQuad(k: 'dual')             // psycho optimised quad
    //decoder = FoaDecoderMatrix.newQuad(pi/6, 'dual')          // psycho optimised narrow quad
    //decoder = FoaDecoderMatrix.new5_0                         // 5.0
    //decoder = FoaDecoderMatrix.newPanto(6, k: 'dual')         // psycho optimised hex
    

    this.panners = (
      '1': false
    );

    panningResponder = OSCdef.new('panningResponder', {
      arg msg;
      var trackId;

      this.handle_pan_message((
        trackId: msg[1],
        azimuth: msg[2],
        elevation: msg[3],
        distance: msg[4]
      ));

    }, '/cs/from_ableton/panner_update', recvPort: 6666);
    
    super.init(params);
  }

  handle_pan_message {
    arg params;

    "params:".postln;
    params.postln;    
  }

  load_environment {
    this.panners['1'] = Patch(Instr.at("cs.utility.AbletonPanner"), (
      bus: 12
    ));

    this.panners['1'].prepareForPlay();
  }

  on_play {
    this.outputChannel.play(this.panners['1']);
  }

}
