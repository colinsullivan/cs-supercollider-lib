OrganicPercussionSynkopantsElement : SynkopantsElement {

  var <>numVoices,

    <>noteDurationCtl,

    /**
     *  Whether or not to do the frequency sweep
     **/
    <>doFreqSweepCtl,

    /**
     *  Target of the frequency sweep
     **/
    <>freqSweepTargetCtl,

    <>freq;

  init {

    arg params;

    super.init(params);

    this.freq = params['freq'];

    // TODO: use the specs directly from the instrument
    this.noteDurationCtl = KrNumberEditor.new(0.5, \unipolar);
    this.doFreqSweepCtl = KrNumberEditor.new(0, \unipolar);
    this.freqSweepTargetCtl = KrNumberEditor.new(
      1.0,
      ControlSpec(0.01, 10, \exp)
    );
  }

  init_patch {

    this.patch = Patch("cs.fm.OrganicPercussion", (
      freq: this.freq,
      amp: this.ampSlider,
      autoDurationOn: 0,
      noteDuration: this.noteDurationCtl,
      doFreqSweep: this.doFreqSweepCtl,
      freqSweepTargetMultiplier: this.freqSweepTargetCtl,
      gate: 1
    ));
  
  }

  init_gui {
    arg params;

    var layout,
      labelWidth;

    super.init_gui(params);

    layout = params['layout'];
    labelWidth = params['labelWidth'];
    
    ArgNameLabel("noteDuration", layout, labelWidth);
    this.noteDurationCtl.gui(layout);
    layout.startRow();
    
    ArgNameLabel("doSweep", layout, labelWidth);
    this.doFreqSweepCtl.gui(layout);
    layout.startRow();
    
    ArgNameLabel("sweepTarget", layout, labelWidth);
    this.freqSweepTargetCtl.gui(layout);
    layout.startRow();
  }
}
