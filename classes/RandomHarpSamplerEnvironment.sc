RandomHarpSamplerEnvironment : PerformanceEnvironmentComponent {

  // the segments of the buffer we want to play
  var <>bufSegments,
    <>outputBus,
    <>patch,
    // a single voicer which will have voices for each segment
    <>voicer,
    // the buffer
    <>buf,
    // master probability control
    <>probability,
    // master frequency control (wait time between)
    <>waitTime,
    <>willPlayNext,
    <>outputChannel,
    <>playerRoutine;

  init {
    arg params;

    this.bufSegments = [
      (
        start: 0,
        end: 7.2,
        gain: 1.0
      ),
      /*(
        start: 8.2,
        end: 13.0,
        gain: 1.2
      ),*/
      ( 
        start: 14.1,
        end: 17.3,
        gain: 1.4
      ),
      (
        start: 17.4,
        end: 20.5,
        gain: 1.8
      ),
      (
        start: 20.5,
        end: 24.7,
        gain: 1.8
      ),
      (
        start: 24.91,
        end: 35,
        gain: 2.4
      )
    ];

    this.bufSegments.do({
      arg bufSegment;

      bufSegment['gain'] = (1.0 / this.bufSegments.size()) * bufSegment['gain'];
    });

    this.voicer = nil;
    this.patch = nil;

    this.probability = KrNumberEditor.new(0.7, \unipolar, \exp);
    this.waitTime = KrNumberEditor.new(2.0, ControlSpec(4.0, 0.1, \exp));

    this.willPlayNext = false;

    this.outputBus = 2;

    super.init(params);

  }
  load_samples {
    arg callback;
    var sfxRoot = "/Volumes/Secondary/Samples/Recorded Sounds/Sound Effects/",
      me = this;

    Buffer.read(Server.default, sfxRoot ++ "harp-plucks-04_long.aif", action: {
      arg envBuf;

      me.buf = envBuf;

      callback.value();
    });
  }

  play_next_pluck {

    var nextBufSegment;

    if (1.0.rand() < this.probability.value(), {
      nextBufSegment = this.bufSegments.choose();
      "nextBufSegment:".postln;
      nextBufSegment.postln;
      // play
      this.voicer.trigger1(0, 1, [
        \startTime, nextBufSegment['start'],
        \endTime, nextBufSegment['end'],
        \gain, nextBufSegment['gain']
      ]);
    });
  }

  load_environment {
    var me = this;
    super.load_environment();

    this.outputChannel = MixerChannel.new(
      "RandomHarpSamplerEnv",
      Server.default,
      2, 2,
      outbus: this.outputBus
    );

    this.voicer = Voicer.new(
      8,
      Instr.at("cs.sfx.PlayBufSegment"),
      [
        \buf, this.buf,
        \playbackRate, 1.0,
        \attackTime, 0.1,
        \releaseTime, 0.1
      ],
      target: this.outputChannel
    );

    this.playerRoutine = Routine.new({
      loop {
        me.play_next_pluck(); 

        me.waitTime.value().wait();
      }
    });

  }

  on_play {
    this.playerRoutine.play();
  }

  on_stop {
    this.playerRoutine.stop();
  }

  init_gui {
    arg params;

    var layout = params['layout'],
      labelWidth = 50,
      me = this;

    super.init_gui(params);

    layout.flow({
      arg layout;

      ArgNameLabel("prob", layout, labelWidth);
      me.probability.gui(layout);
      layout.startRow();
      
      ArgNameLabel("wait", layout, labelWidth);
      me.waitTime.gui(layout);
      layout.startRow();
      
    });
  }

}
