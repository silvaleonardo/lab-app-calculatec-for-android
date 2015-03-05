package com.silvaleonardo.calculatec;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CalculadoraActivity extends Activity {
	
	private Calculadora calc;
	
	private boolean usuarioEstaDigitandoUmNumero;
	private boolean separadorDecimalFoiDigitado;
	private TextView txtVisor;
	
	private String separador;
	private char separadorChar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        
        calc = new Calculadora();
        usuarioEstaDigitandoUmNumero = false;
        separadorDecimalFoiDigitado = false;
        
        txtVisor = (TextView)findViewById(R.id.txtVisor);
        txtVisor.setText("0");
        
        Locale localizacao = getResources().getConfiguration().locale;
        NumberFormat formatador = NumberFormat.getInstance(localizacao);
        
        //Definimos inicialmente o separador como vírgula, que é o padrão nacional
        separadorChar = ',';
        if(formatador instanceof DecimalFormat){
        	DecimalFormatSymbols simbolo = ((DecimalFormat)formatador).getDecimalFormatSymbols();
        	separadorChar = simbolo.getDecimalSeparator();
        }
        separador = String.valueOf(separadorChar);
        //Não se esqueça de trocar aqui o id do seu botão separador, se necessário!
        Button btnSepardor = (Button)findViewById(R.id.button19);
        btnSepardor.setText(separador);
        
        //Uso da fonte digital
        final Typeface fonteDigital = Typeface.createFromAsset(this.getAssets(), "digital.ttf");
        txtVisor.setTypeface(fonteDigital);
        
        txtVisor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(txtVisor.getTypeface().equals(fonteDigital)){
					txtVisor.setTypeface(Typeface.DEFAULT);
				}else{
					txtVisor.setTypeface(fonteDigital);
				}
				
			}
		});
        Toast.makeText(this, "Toque no visor para trocar sua fonte!", Toast.LENGTH_LONG).show();
        
    }
    
    public void onClickNumeros(View v){
    	
    	Button botaoTocado = (Button)v;
    	String digito = botaoTocado.getText().toString();
    	
    	String textoNoVisor = txtVisor.getText().toString();
    	
    	if(!usuarioEstaDigitandoUmNumero || textoNoVisor.equals("0")){
    		txtVisor.setText(digito);
    		if(!digito.equals("0")){
    			usuarioEstaDigitandoUmNumero = true;
    		}
    	}else{
    		txtVisor.setText(textoNoVisor + digito);
    	}
    	
    }
    
    public void onClickOperacoes(View v){
    	
    	Button botaoTocado = (Button)v;
    	String operacao = botaoTocado.getText().toString();
    	
    	if(operacao.equals(separador) && !separadorDecimalFoiDigitado){
    		separadorDecimalFoiDigitado = true;
    		if(!usuarioEstaDigitandoUmNumero)
    			txtVisor.setText("0" + separador);
    		else
    			txtVisor.setText(txtVisor.getText().toString() + separador);
    		usuarioEstaDigitandoUmNumero = true;
    		
    	}else if(!operacao.equals(separador)){
    		String valorSemVirgula = txtVisor.getText().toString().replace(separadorChar, '.');
    		calc.setOperando(Double.parseDouble(valorSemVirgula));
    		calc.realizarOperacao(operacao);
    		String textoResultado = String.valueOf(calc.getOperando());
    		if(textoResultado.endsWith(".0")){
    			textoResultado = textoResultado.substring(0, textoResultado.length() - 2);
    		}
    		txtVisor.setText(textoResultado.replace('.', separadorChar));
    		usuarioEstaDigitandoUmNumero = false;
    		separadorDecimalFoiDigitado = false;
    	}
    	
    }
    
    public void onClickMemoria(View v){
    	
    	Button botaoTocado = (Button)v;
    	String operacaoMemoria = botaoTocado.getText().toString();
    	String valorSemVirgula = txtVisor.getText().toString().replace(separadorChar, '.');
    	calc.setOperando(Double.parseDouble(valorSemVirgula));
    	calc.realizarOperacaoDeMemoria(operacaoMemoria);
    	usuarioEstaDigitandoUmNumero = false;
    	
    }
    
}
