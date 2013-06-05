(

  s.quit;

  s.options.inDevice = "JackRouter";
  s.options.outDevice = "JackRouter";
  s.options.numOutputBusChannels = 32;


  s.options.blockSize = 8;
  s.boot();

  s.doWhenBooted({
    "Hello!".postln();
    
    // ------------------------------------------------------------
    // choose a decoder

    // stereophonic / binaural
    // Cardioids at 131 deg
    //~decoder = FoaDecoderMatrix.newStereo(131/2 * pi/180, 0.5);
    // UHJ (kernel)
    //~decoder = FoaDecoderKernel.newUHJ;
    // synthetic binaural (kernel)
    ~decoder = FoaDecoderKernel.newSpherical();
    // KEMAR binaural (kernel)
    //~decoder = FoaDecoderKernel.newCIPIC();


    // pantophonic (2D)
    // psycho optimised quad
    ~decoder = FoaDecoderMatrix.newQuad(k: 'dual');
    // psycho optimised narrow quad
    //~decoder = FoaDecoderMatrix.newQuad(pi/6, 'dual');
    // 5.0
    //~decoder = FoaDecoderMatrix.new5_0();
    // psycho optimised hex
    //~decoder = FoaDecoderMatrix.newPanto(6, k: 'dual');


    // periphonic (3D)
    // psycho optimised cube
    //~decoder = FoaDecoderMatrix.newPeri(k: 'dual');
    // psycho optimised bi-rectangle
    //~decoder = FoaDecoderMatrix.newDiametric(
      //pi/180 * [[30, 0],
      //[-30, 0],
      //[90, 35.3],
      //[-90, 35.3]],
      //'dual'
    //)

    // inspect
    ~decoder.kind.postln();


    // ------------------------------------------------------------
    // define ~renderDecode
    (
    ~renderDecode = { arg in, decoder;
        var kind;
        var fl, bl, br, fr;
        var fc, lo;
        var sl, sr;
        var flu, blu, bru, fru;
        var fld, bld, brd, frd;
        var slu, sru, sld, srd;

        kind = decoder.kind;
        
        "in renderDecode".postln;
        kind.postln;
            
        case
            { decoder.numChannels == 2 }
                {
                        
                        "in stereo".postln;
                        
                    // decode to stereo (or binaural)
                    FoaDecode.ar(in, ~decoder)
                }
            { kind == 'quad' }
                {
                        "in quad".postln;
                        
                    // decode (to quad)
                    #fl, bl, br, fr = FoaDecode.ar(in, ~decoder);

                    // reorder output to match speaker arrangement
                    [fl, fr, bl, br]
                }
            { kind == '5.0' }
                {
                    // decode (to 5.0)
                        "in 5.0".postln;
                        
                    #fc, fl, bl, br, fr = FoaDecode.ar(in, ~decoder);
                    lo = Silent.ar;
                                    
                    // reorder output to match speaker arrangement
                    [fl, fr, fc, lo, bl, br]
                }
            { kind == 'panto' }
                {
                        "in panto".postln;
                        
                    // decode (to hex)
                    #fl, sl, bl, br, sr, fr = FoaDecode.ar(in, ~decoder);
                                    
                    // reorder output to match speaker arrangement
                    [fl, fr, sl, sr, bl, br]
                }
            { kind == 'peri' }
                {
                        "in peri".postln;
                        
                    // decode (to cube)
                    #flu, blu, bru, fru, fld, bld, brd, frd = FoaDecode.ar(in, ~decoder);
                                    
                    // reorder output to match speaker arrangement
                    [flu, fru, blu, bru, fld, frd, bld, brd]
                }
            { kind == 'diametric' }
                {
                        "in diametric".postln;
                        
                    // decode (to bi-rectangle)
                    #fl, fr, slu, sru, br, bl, srd, sld = FoaDecode.ar(in, ~decoder);
                    
                    // reorder output to match speaker arrangement
                    [fl, fr, bl, br, slu, sru, sld, srd]
                };
    }
    )

    // ------------------------------------------------------------
    // now we're ready to try the examples below!
    // ------------------------------------------------------------
  });

);
