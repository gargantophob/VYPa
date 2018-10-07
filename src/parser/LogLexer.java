// Generated from Log.g4 by ANTLR 4.7.1
package parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LogLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, DATE=5, TIME=6, STRING=7, CRLF=8, WHITESPACE=9;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "DIGIT", "TWODIGIT", "LETTER", "DATE", 
		"TIME", "STRING", "CRLF", "WHITESPACE"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "' '", "'ERROR'", "'INFO'", "'DEBUG'", null, null, null, null, "'\n'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, "DATE", "TIME", "STRING", "CRLF", "WHITESPACE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public LogLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Log.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\13T\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3"+
		"\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\6\13F\n\13\r\13\16\13"+
		"G\3\f\5\fK\n\f\3\f\3\f\5\fO\n\f\3\r\3\r\3\r\3\r\2\2\16\3\3\5\4\7\5\t\6"+
		"\13\2\r\2\17\2\21\7\23\b\25\t\27\n\31\13\3\2\4\3\2\62;\4\2C\\c|\2S\2\3"+
		"\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2"+
		"\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\3\33\3\2\2\2\5\35\3\2\2\2\7#\3"+
		"\2\2\2\t(\3\2\2\2\13.\3\2\2\2\r\60\3\2\2\2\17\63\3\2\2\2\21\65\3\2\2\2"+
		"\23>\3\2\2\2\25E\3\2\2\2\27N\3\2\2\2\31P\3\2\2\2\33\34\7\"\2\2\34\4\3"+
		"\2\2\2\35\36\7G\2\2\36\37\7T\2\2\37 \7T\2\2 !\7Q\2\2!\"\7T\2\2\"\6\3\2"+
		"\2\2#$\7K\2\2$%\7P\2\2%&\7H\2\2&\'\7Q\2\2\'\b\3\2\2\2()\7F\2\2)*\7G\2"+
		"\2*+\7D\2\2+,\7W\2\2,-\7I\2\2-\n\3\2\2\2./\t\2\2\2/\f\3\2\2\2\60\61\5"+
		"\13\6\2\61\62\5\13\6\2\62\16\3\2\2\2\63\64\t\3\2\2\64\20\3\2\2\2\65\66"+
		"\5\r\7\2\66\67\5\r\7\2\678\7/\2\289\5\17\b\29:\5\17\b\2:;\5\17\b\2;<\7"+
		"/\2\2<=\5\r\7\2=\22\3\2\2\2>?\5\r\7\2?@\7<\2\2@A\5\r\7\2AB\7<\2\2BC\5"+
		"\r\7\2C\24\3\2\2\2DF\5\17\b\2ED\3\2\2\2FG\3\2\2\2GE\3\2\2\2GH\3\2\2\2"+
		"H\26\3\2\2\2IK\7\17\2\2JI\3\2\2\2JK\3\2\2\2KL\3\2\2\2LO\7\f\2\2MO\7\17"+
		"\2\2NJ\3\2\2\2NM\3\2\2\2O\30\3\2\2\2PQ\7\f\2\2QR\3\2\2\2RS\b\r\2\2S\32"+
		"\3\2\2\2\6\2GJN\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}