package jp.sourceforge.pdt_tools.formatter.prototype.preferences;

import jp.sourceforge.pdt_tools.formatter.prototype.FormatterPrototypePlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.php.internal.core.PHPCoreConstants;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = new ScopedPreferenceStore(new InstanceScope(), FormatterPrototypePlugin.PLUGIN_ID);

		store.setDefault(PreferenceConstants.INDENT_WITH_TAB, true);
		store.setDefault(PreferenceConstants.INDENT_SPACES, 4);
		store.setDefault(PreferenceConstants.INDENT_BASE, 0);
		store.setDefault(PreferenceConstants.LINE_LENGTH, 0);
		store.setDefault(PreferenceConstants.NEW_LINE_FOR_CLASS, false);
		store.setDefault(PreferenceConstants.NEW_LINE_FOR_FUNCTION, false);
		store.setDefault(PreferenceConstants.NEW_LINE_FOR_TRY_CATCH, false);
		store.setDefault(PreferenceConstants.NEW_LINE_FOR_CATCH, false);
		store.setDefault(PreferenceConstants.NEW_LINE_FOR_ELSE, false);
		store.setDefault(PreferenceConstants.NEW_LINE_FOR_BLOCK, false);
		store.setDefault(PreferenceConstants.INDENT_CASE_BLOCK, false);
		store.setDefault(PreferenceConstants.SPACER_FOR_CONCAT, false);
		store.setDefault(PreferenceConstants.SPACER_FOR_ARRAY_ARROW, true);
		store.setDefault(PreferenceConstants.SPACER_FOR_FUNCTION_DEF, false);
		store.setDefault(PreferenceConstants.SPACER_FOR_COMMENT, true);
		store.setDefault(PreferenceConstants.SPACER_FOR_SHORTCUT, true);
		store.setDefault(PreferenceConstants.SPACER_FOR_SHORTCUT_CLOSE, true);
		store.setDefault(PreferenceConstants.SPACER_FOR_CAST, true);
		store.setDefault(PreferenceConstants.SPACER_AFTER_CONTROL, true);
		store.setDefault(PreferenceConstants.SPACER_BEFORE_CURLY, true);
		store.setDefault(PreferenceConstants.SPACER_AFTER_CURLY, true);
		store.setDefault(PreferenceConstants.LEAVE_BLANK_LINES1, true);
		store.setDefault(PreferenceConstants.LEAVE_BLANK_LINES2, true);
		store.setDefault(PreferenceConstants.LEAVE_BLANK_LINES3, true);
		store.setDefault(PreferenceConstants.SHRINK_BLANK_LINES1, true);
		store.setDefault(PreferenceConstants.SHRINK_BLANK_LINES2, true);
		store.setDefault(PreferenceConstants.SHRINK_BLANK_LINES3, true);
		store.setDefault(PreferenceConstants.LEAVE_NEWLINE_IN_ARRAY, true);
		store.setDefault(PreferenceConstants.LEAVE_NEWLINE_AFTER_COMMA, false);
		store.setDefault(PreferenceConstants.LEAVE_NEWLINE_WITH_CONCAT_OP, false);
		store.setDefault(PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW, false);
		store.setDefault(PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW_NEST, false);
		store.setDefault(PreferenceConstants.JOIN_CONCAT_OP_TO_PREV_LINE, false);
		store.setDefault(PreferenceConstants.JOIN_CONCAT_OP_TO_POST_LINE, false);
		store.setDefault(PreferenceConstants.ALIGN_DOUBLE_ARROW, false);
		store.setDefault(PreferenceConstants.ALIGN_DOUBLE_ARROW_WITH_TAB, false);
		store.setDefault(PreferenceConstants.EQUATE_ELSE_IF_TO_ELSEIF, true);
		store.setDefault(PreferenceConstants.SIMPLE_STATEMENT_IN_ONE_LINE, false);
		store.setDefault(PreferenceConstants.COMPACT_EMPTY_BLOCK, false);
		store.setDefault(PreferenceConstants.PROJECT, "");

		store.setDefault(PHPCoreConstants.FORMATTER_USE_TABS, true);
		store.setDefault(PHPCoreConstants.FORMATTER_INDENTATION_SIZE, 4);
	}

	public void resetPreference(IPreferenceStore store) {
		store.setToDefault(PreferenceConstants.INDENT_WITH_TAB);
		store.setToDefault(PreferenceConstants.INDENT_SPACES);
		store.setToDefault(PreferenceConstants.INDENT_BASE);
		store.setToDefault(PreferenceConstants.LINE_LENGTH);
		store.setToDefault(PreferenceConstants.NEW_LINE_FOR_CLASS);
		store.setToDefault(PreferenceConstants.NEW_LINE_FOR_FUNCTION);
		store.setToDefault(PreferenceConstants.NEW_LINE_FOR_TRY_CATCH);
		store.setToDefault(PreferenceConstants.NEW_LINE_FOR_CATCH);
		store.setToDefault(PreferenceConstants.NEW_LINE_FOR_ELSE);
		store.setToDefault(PreferenceConstants.NEW_LINE_FOR_BLOCK);
		store.setToDefault(PreferenceConstants.INDENT_CASE_BLOCK);
		store.setToDefault(PreferenceConstants.SPACER_FOR_CONCAT);
		store.setToDefault(PreferenceConstants.SPACER_FOR_ARRAY_ARROW);
		store.setToDefault(PreferenceConstants.SPACER_FOR_FUNCTION_DEF);
		store.setToDefault(PreferenceConstants.SPACER_FOR_COMMENT);
		store.setToDefault(PreferenceConstants.SPACER_FOR_SHORTCUT);
		store.setToDefault(PreferenceConstants.SPACER_FOR_SHORTCUT_CLOSE);
		store.setToDefault(PreferenceConstants.SPACER_FOR_CAST);
		store.setToDefault(PreferenceConstants.SPACER_AFTER_CONTROL);
		store.setToDefault(PreferenceConstants.SPACER_BEFORE_CURLY);
		store.setToDefault(PreferenceConstants.SPACER_AFTER_CURLY);
		store.setToDefault(PreferenceConstants.LEAVE_BLANK_LINES1);
		store.setToDefault(PreferenceConstants.LEAVE_BLANK_LINES2);
		store.setToDefault(PreferenceConstants.LEAVE_BLANK_LINES3);
		store.setToDefault(PreferenceConstants.SHRINK_BLANK_LINES1);
		store.setToDefault(PreferenceConstants.SHRINK_BLANK_LINES2);
		store.setToDefault(PreferenceConstants.SHRINK_BLANK_LINES3);
		store.setToDefault(PreferenceConstants.LEAVE_NEWLINE_IN_ARRAY);
		store.setToDefault(PreferenceConstants.LEAVE_NEWLINE_AFTER_COMMA);
		store.setToDefault(PreferenceConstants.LEAVE_NEWLINE_WITH_CONCAT_OP);
		store.setToDefault(PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW);
		store.setToDefault(PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW_NEST);
		store.setToDefault(PreferenceConstants.JOIN_CONCAT_OP_TO_PREV_LINE);
		store.setToDefault(PreferenceConstants.JOIN_CONCAT_OP_TO_POST_LINE);
		store.setToDefault(PreferenceConstants.ALIGN_DOUBLE_ARROW);
		store.setToDefault(PreferenceConstants.ALIGN_DOUBLE_ARROW_WITH_TAB);
		store.setToDefault(PreferenceConstants.EQUATE_ELSE_IF_TO_ELSEIF);
		store.setToDefault(PreferenceConstants.SIMPLE_STATEMENT_IN_ONE_LINE);
		store.setToDefault(PreferenceConstants.COMPACT_EMPTY_BLOCK);
		store.setToDefault(PreferenceConstants.PROJECT);

		store.setToDefault(PHPCoreConstants.FORMATTER_USE_TABS);
		store.setToDefault(PHPCoreConstants.FORMATTER_INDENTATION_SIZE);
	}

	public void copyPreference(IPreferenceStore master, IPreferenceStore slave) {
		copyBooleanValue(master, slave, PreferenceConstants.INDENT_WITH_TAB);
		copyIntValue    (master, slave, PreferenceConstants.INDENT_SPACES);
		copyIntValue    (master, slave, PreferenceConstants.INDENT_BASE);
		copyIntValue    (master, slave, PreferenceConstants.LINE_LENGTH);
		copyBooleanValue(master, slave, PreferenceConstants.NEW_LINE_FOR_CLASS);
		copyBooleanValue(master, slave, PreferenceConstants.NEW_LINE_FOR_FUNCTION);
		copyBooleanValue(master, slave, PreferenceConstants.NEW_LINE_FOR_TRY_CATCH);
		copyBooleanValue(master, slave, PreferenceConstants.NEW_LINE_FOR_CATCH);
		copyBooleanValue(master, slave, PreferenceConstants.NEW_LINE_FOR_ELSE);
		copyBooleanValue(master, slave, PreferenceConstants.NEW_LINE_FOR_BLOCK);
		copyBooleanValue(master, slave, PreferenceConstants.INDENT_CASE_BLOCK);
		copyBooleanValue(master, slave, PreferenceConstants.SPACER_FOR_CONCAT);
		copyBooleanValue(master, slave, PreferenceConstants.SPACER_FOR_ARRAY_ARROW);
		copyBooleanValue(master, slave, PreferenceConstants.SPACER_FOR_FUNCTION_DEF);
		copyBooleanValue(master, slave, PreferenceConstants.SPACER_FOR_COMMENT);
		copyBooleanValue(master, slave, PreferenceConstants.SPACER_FOR_SHORTCUT);
		copyBooleanValue(master, slave, PreferenceConstants.SPACER_FOR_SHORTCUT_CLOSE);
		copyBooleanValue(master, slave, PreferenceConstants.SPACER_FOR_CAST);
		copyBooleanValue(master, slave, PreferenceConstants.SPACER_AFTER_CONTROL);
		copyBooleanValue(master, slave, PreferenceConstants.SPACER_BEFORE_CURLY);
		copyBooleanValue(master, slave, PreferenceConstants.SPACER_AFTER_CURLY);
		copyBooleanValue(master, slave, PreferenceConstants.LEAVE_BLANK_LINES1);
		copyBooleanValue(master, slave, PreferenceConstants.LEAVE_BLANK_LINES2);
		copyBooleanValue(master, slave, PreferenceConstants.LEAVE_BLANK_LINES3);
		copyBooleanValue(master, slave, PreferenceConstants.SHRINK_BLANK_LINES1);
		copyBooleanValue(master, slave, PreferenceConstants.SHRINK_BLANK_LINES2);
		copyBooleanValue(master, slave, PreferenceConstants.SHRINK_BLANK_LINES3);
		copyBooleanValue(master, slave, PreferenceConstants.LEAVE_NEWLINE_IN_ARRAY);
		copyBooleanValue(master, slave, PreferenceConstants.LEAVE_NEWLINE_AFTER_COMMA);
		copyBooleanValue(master, slave, PreferenceConstants.LEAVE_NEWLINE_WITH_CONCAT_OP);
		copyBooleanValue(master, slave, PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW);
		copyBooleanValue(master, slave, PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW_NEST);
		copyBooleanValue(master, slave, PreferenceConstants.JOIN_CONCAT_OP_TO_PREV_LINE);
		copyBooleanValue(master, slave, PreferenceConstants.JOIN_CONCAT_OP_TO_POST_LINE);
		copyBooleanValue(master, slave, PreferenceConstants.ALIGN_DOUBLE_ARROW);
		copyBooleanValue(master, slave, PreferenceConstants.ALIGN_DOUBLE_ARROW_WITH_TAB);
		copyBooleanValue(master, slave, PreferenceConstants.EQUATE_ELSE_IF_TO_ELSEIF);
		copyBooleanValue(master, slave, PreferenceConstants.SIMPLE_STATEMENT_IN_ONE_LINE);
		copyBooleanValue(master, slave, PreferenceConstants.COMPACT_EMPTY_BLOCK);
		copyStringValue (master, slave, PreferenceConstants.PROJECT);

		copyBooleanValue(master, slave, PHPCoreConstants.FORMATTER_USE_TABS);
		copyIntValue    (master, slave, PHPCoreConstants.FORMATTER_INDENTATION_SIZE);
	}

	private void copyBooleanValue(IPreferenceStore master, IPreferenceStore slave, String name) {
		slave.setValue(name, master.getBoolean(name));
	}

	private void copyIntValue(IPreferenceStore master, IPreferenceStore slave, String name) {
		slave.setValue(name, master.getInt(name));
	}

	private void copyStringValue(IPreferenceStore master, IPreferenceStore slave, String name) {
		slave.setValue(name, master.getString(name));
	}

}
