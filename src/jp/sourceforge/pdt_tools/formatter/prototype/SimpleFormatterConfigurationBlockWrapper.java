package jp.sourceforge.pdt_tools.formatter.prototype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jp.sourceforge.pdt_tools.formatter.prototype.preferences.Messages;
import jp.sourceforge.pdt_tools.formatter.prototype.preferences.PreferenceConstants;
import jp.sourceforge.pdt_tools.formatter.prototype.preferences.PreferenceInitializer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Region;
import org.eclipse.php.internal.core.PHPCoreConstants;
import org.eclipse.php.internal.core.documentModel.provisional.contenttype.ContentTypeIdForPHP;
import org.eclipse.php.internal.ui.editor.PHPStructuredTextViewer;
import org.eclipse.php.internal.ui.editor.configuration.PHPStructuredTextViewerConfiguration;
import org.eclipse.php.internal.ui.preferences.IStatusChangeListener;
import org.eclipse.php.ui.preferences.IPHPFormatterConfigurationBlockWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;

public class SimpleFormatterConfigurationBlockWrapper implements
		IPHPFormatterConfigurationBlockWrapper {

	private IPreferenceStore pref;
	private Button indentWithTab;
	private Button indentWithSpace;
	private Spinner spaceAmount;
	private Spinner baseIndent;
	private Spinner lineLength;
	private Button leaveBlankLines1;
	private Button shrinkBlankLines1;
	private Button leaveBlankLines2;
	private Button shrinkBlankLines2;
	private Button leaveBlankLines3;
	private Button shrinkBlankLines3;
	private Button leaveNewLineWithConcatOp;
	private Button joinConcatOpToPrevLine;
	private Button joinConcatOpToPostLine;
	private Button leaveNewLineForArrow;
	private Button leaveNewLineForArrowNest;
	private Button alignDoubleArrow;
	private Button alignDoubleArrowWithSpace;
	private Button alignDoubleArrowWithTab;
	private ArrayList<FormattingOption> formattingOptions;
	private IStructuredDocument document;

	private IProject project = null;
	private boolean useProject = false;

	public Control createContents(Composite composite) {
		GridLayout layout = (GridLayout) composite.getLayout();
		layout.numColumns = 2;

		formattingOptions = new ArrayList<FormattingOption>();
		SelectionListener listener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				preview();
			}
		};

		Composite base = new Composite(composite, SWT.NONE);
		base.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		base.setLayout(new GridLayout(1, true));

		Group indentation = new Group(base, SWT.NONE);
		indentation.setText(Messages.getString("Indentation.label")); //$NON-NLS-1$
		indentation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		indentation.setLayout(new GridLayout(2, true));

		Composite selection = new Composite(indentation, SWT.NONE);
		selection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout gdSelection = new GridLayout(2, false);
		gdSelection.marginHeight = 2;
		gdSelection.marginWidth = 5;
		selection.setLayout(gdSelection);
		indentWithTab = new Button(selection, SWT.RADIO);
		indentWithTab.setText(Messages.getString("Indentation.Tab.caption")); //$NON-NLS-1$
		GridData gd2 = new GridData();
		gd2.horizontalSpan = 2;
		indentWithTab.setLayoutData(gd2);
		indentWithTab.addSelectionListener(listener);
		indentWithSpace = new Button(selection, SWT.RADIO);
		indentWithSpace.setText(Messages.getString("Indentation.Space.caption")); //$NON-NLS-1$
		indentWithSpace.setLayoutData(new GridData());
		indentWithSpace.addSelectionListener(listener);
		spaceAmount = new Spinner(selection, SWT.BORDER);
		spaceAmount.setValues(4, 1, 99, 0, 1, 4);
		spaceAmount.setLayoutData(new GridData());
		spaceAmount.addSelectionListener(listener);

		Composite baseIndentation = new Composite(indentation, SWT.NONE);
		baseIndentation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout gdBaseIndentation = new GridLayout(2, false);
		gdBaseIndentation.marginHeight = 2;
		gdBaseIndentation.marginWidth = 5;
		baseIndentation.setLayout(gdBaseIndentation);
		Label label1 = new Label(baseIndentation, SWT.NONE);
		label1.setText(Messages.getString("Indentation.Base.caption")); //$NON-NLS-1$
		label1.setLayoutData(new GridData());
		baseIndent = new Spinner(baseIndentation, SWT.BORDER);
		baseIndent.setValues(0, 0, 99, 0, 1, 4);
		baseIndent.setLayoutData(new GridData());
		baseIndent.addSelectionListener(listener);
		Label label2 = new Label(baseIndentation, SWT.NONE);
		label2.setText(Messages.getString("Indentation.Length.caption")); //$NON-NLS-1$
		label2.setLayoutData(new GridData());
		lineLength = new Spinner(baseIndentation, SWT.BORDER);
		lineLength.setValues(0, 0, 9999, 0, 1, 10);
		lineLength.setLayoutData(new GridData());
		lineLength.addSelectionListener(listener);

		final Group formatting = new Group(base, SWT.NONE);
		formatting.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		((GridData)formatting.getLayoutData()).horizontalSpan = 2;
		formatting.setText(Messages.getString("Formatting.label")); //$NON-NLS-1$
		formatting.setLayout(new GridLayout());

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		final Composite outerFrame = new Composite(formatting, SWT.V_SCROLL);
		outerFrame.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		outerFrame.setLayout(gridLayout);
		outerFrame.setSize(400, 400);
		final Composite innerFrame = new Composite(outerFrame, SWT.NONE);
		innerFrame.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		innerFrame.setLayout(gridLayout);
		final ScrollBar scrollBar = outerFrame.getVerticalBar();
		scrollBar.setMinimum(0);
		scrollBar.setIncrement(12);
		scrollBar.setPageIncrement(48);
		scrollBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Point location = innerFrame.getLocation();
				location.y = -scrollBar.getSelection();
				innerFrame.setLocation(location);
			}
		});
		ExpansionAdapter adapter = new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				Point location = innerFrame.getLocation();
				innerFrame.pack(true);
				Point innerSize = innerFrame.getSize();
				Point outerSize = outerFrame.getSize();
				int y = innerSize.y - outerSize.y;
				if (y > 0) {
					scrollBar.setEnabled(true);
					scrollBar.setMaximum(y + scrollBar.getIncrement());
					scrollBar.setSelection(-location.y);
				} else {
					scrollBar.setEnabled(false);
					innerFrame.setLocation(new Point(0, 0));
				}
			}
		};

		ExpandableComposite expNewLines = new ExpandableComposite(innerFrame, SWT.NONE,
				ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT);
		expNewLines.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		expNewLines.setText(Messages.getString("Formatting.NewLineGroup.label")); //$NON-NLS-1$
		expNewLines.addExpansionListener(adapter);
		Composite optNewLines = new Composite(expNewLines, SWT.NONE);
		optNewLines.setLayout(new GridLayout());
		formattingOptions.add(new FormattingOption(optNewLines,
				PreferenceConstants.NEW_LINE_FOR_CLASS,
				"Formatting.NewLineForClass.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optNewLines,
				PreferenceConstants.NEW_LINE_FOR_FUNCTION,
				"Formatting.NewLineForFunction.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optNewLines,
				PreferenceConstants.NEW_LINE_FOR_TRY_CATCH,
				"Formatting.NewLineForTryCatch.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optNewLines,
				PreferenceConstants.NEW_LINE_FOR_CATCH,
				"Formatting.NewLineForCatch.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optNewLines,
				PreferenceConstants.NEW_LINE_FOR_ELSE,
				"Formatting.NewLineForElse.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optNewLines,
				PreferenceConstants.NEW_LINE_FOR_BLOCK,
				"Formatting.NewLineForBlock.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optNewLines,
				PreferenceConstants.LEAVE_NEWLINE_IN_ARRAY,
				"Formatting.LeaveNewLineInArray.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optNewLines,
				PreferenceConstants.LEAVE_NEWLINE_AFTER_COMMA,
				"Formatting.LeaveNewLineAfterComma.caption")); //$NON-NLS-1$

		leaveNewLineWithConcatOp = new Button(optNewLines, SWT.CHECK);
		leaveNewLineWithConcatOp.setText(
				Messages.getString("Formatting.LeaveNewLineWithConcatOp.caption")); //$NON-NLS-1$
		GridData gd16 = new GridData();
		gd16.horizontalIndent = 16;
		gd16.horizontalAlignment = SWT.FILL;
		joinConcatOpToPrevLine = new Button(optNewLines, SWT.CHECK);
		joinConcatOpToPrevLine.setText(
				Messages.getString("Formatting.JoinConcatOpToPrevLine.caption")); //$NON-NLS-1$
		joinConcatOpToPrevLine.setLayoutData(gd16);
		joinConcatOpToPostLine = new Button(optNewLines, SWT.CHECK);
		joinConcatOpToPostLine.setText(
				Messages.getString("Formatting.JoinConcatOpToPostLine.caption")); //$NON-NLS-1$
		joinConcatOpToPostLine.setLayoutData(gd16);
		leaveNewLineWithConcatOp.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				boolean enabled = leaveNewLineWithConcatOp.getSelection();
				joinConcatOpToPrevLine.setEnabled(enabled);
				joinConcatOpToPostLine.setEnabled(enabled);
				preview();
			}
		});
		joinConcatOpToPrevLine.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				if (joinConcatOpToPrevLine.getSelection()) {
					if (joinConcatOpToPostLine.getSelection()) {
						joinConcatOpToPostLine.setSelection(false);
					}
				}
				preview();
			}
		});
		joinConcatOpToPostLine.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				if (joinConcatOpToPostLine.getSelection()) {
					if (joinConcatOpToPrevLine.getSelection()) {
						joinConcatOpToPrevLine.setSelection(false);
					}
				}
				preview();
			}
		});
		leaveNewLineForArrow = new Button(optNewLines, SWT.CHECK);
		leaveNewLineForArrow.setText(
				Messages.getString("Formatting.LeaveNewLineForArrow.caption")); //$NON-NLS-1$
		leaveNewLineForArrowNest = new Button(optNewLines, SWT.CHECK);
		leaveNewLineForArrowNest.setText(
				Messages.getString("Formatting.LeaveNewLineForArrowNest.caption")); //$NON-NLS-1$
		leaveNewLineForArrowNest.setLayoutData(gd16);
		leaveNewLineForArrow.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				boolean enabled = leaveNewLineForArrow.getSelection();
				leaveNewLineForArrowNest.setEnabled(enabled);
				preview();
			}
		});

		formattingOptions.add(new FormattingOption(optNewLines,
				PreferenceConstants.EQUATE_ELSE_IF_TO_ELSEIF,
				"Formatting.EquateElseIfToElseif.caption")); //$NON-NLS-1$
		//XXX #16894
		formattingOptions.add(new FormattingOption(optNewLines,
				PreferenceConstants.SIMPLE_STATEMENT_IN_ONE_LINE,
				"Formatting.SimpleStatementInOneLine.caption")); //$NON-NLS-1$
		//XXX 2009/05/25
		formattingOptions.add(new FormattingOption(optNewLines,
				PreferenceConstants.COMPACT_EMPTY_BLOCK,
				"Formatting.CompactEmptyBlock.caption")); //$NON-NLS-1$

		expNewLines.setClient(optNewLines);
		expNewLines.setExpanded(true);

		ExpandableComposite expSpacer = new ExpandableComposite(innerFrame, SWT.NONE,
				ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT);
		expSpacer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		expSpacer.setText(Messages.getString("Formatting.SpacerGroup.label")); //$NON-NLS-1$
		expSpacer.addExpansionListener(adapter);
		Composite optSpacer = new Composite(expSpacer, SWT.NONE);
		optSpacer.setLayout(new GridLayout());
		formattingOptions.add(new FormattingOption(optSpacer,
				PreferenceConstants.SPACER_FOR_CONCAT,
				"Formatting.SpacerForConcat.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optSpacer,
				PreferenceConstants.SPACER_FOR_ARRAY_ARROW,
				"Formatting.SpacerForArrayArrow.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optSpacer,
				PreferenceConstants.SPACER_FOR_FUNCTION_DEF,
				"Formatting.SpacerForFunctionDef.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optSpacer,
				PreferenceConstants.SPACER_FOR_COMMENT,
				"Formatting.SpacerForComment.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optSpacer,
				PreferenceConstants.SPACER_FOR_SHORTCUT,
				"Formatting.SpacerForShortcut.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optSpacer,
				PreferenceConstants.SPACER_FOR_SHORTCUT_CLOSE,
				"Formatting.SpacerForShortcutClose.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optSpacer,
				PreferenceConstants.SPACER_AFTER_CONTROL,
				"Formatting.SpacerAfterControl.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optSpacer,
				PreferenceConstants.SPACER_BEFORE_CURLY,
				"Formatting.SpacerBeforeCurly.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optSpacer,
				PreferenceConstants.SPACER_AFTER_CURLY,
				"Formatting.SpacerAfterCurly.caption")); //$NON-NLS-1$
		formattingOptions.add(new FormattingOption(optSpacer,
				PreferenceConstants.SPACER_FOR_CAST,
				"Formatting.SpacerForCast.caption")); //$NON-NLS-1$
		expSpacer.setClient(optSpacer);

		ExpandableComposite expMisc = new ExpandableComposite(innerFrame, SWT.NONE,
				ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT);
		expMisc.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		expMisc.setText(Messages.getString("Formatting.MiscGroup.label")); //$NON-NLS-1$
		expMisc.addExpansionListener(adapter);
		Composite optMisc = new Composite(expMisc, SWT.NONE);
		optMisc.setLayout(new GridLayout());
		formattingOptions.add(new FormattingOption(optMisc,
				PreferenceConstants.INDENT_CASE_BLOCK,
				"Formatting.IndentCaseBlock.caption")); //$NON-NLS-1$

		//XXX 16941
		leaveBlankLines1 = new Button(optMisc, SWT.CHECK);
		leaveBlankLines1.setText(
				Messages.getString("Formatting.LeaveBlankLines1.caption")); //$NON-NLS-1$
		leaveBlankLines1.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				shrinkBlankLines1.setEnabled(leaveBlankLines1.getSelection());
				preview();
			}
		});
		shrinkBlankLines1 = new Button(optMisc, SWT.CHECK);
		shrinkBlankLines1.setText(
				Messages.getString("Formatting.ShrinkBlankLines1.caption")); //$NON-NLS-1$
		shrinkBlankLines1.setLayoutData(gd16);
		shrinkBlankLines1.setEnabled(leaveBlankLines1.getSelection());
		shrinkBlankLines1.addSelectionListener(listener);

		leaveBlankLines2 = new Button(optMisc, SWT.CHECK);
		leaveBlankLines2.setText(
				Messages.getString("Formatting.LeaveBlankLines2.caption")); //$NON-NLS-1$
		leaveBlankLines2.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				shrinkBlankLines2.setEnabled(leaveBlankLines2.getSelection());
				preview();
			}
		});
		shrinkBlankLines2 = new Button(optMisc, SWT.CHECK);
		shrinkBlankLines2.setText(
				Messages.getString("Formatting.ShrinkBlankLines2.caption")); //$NON-NLS-1$
		shrinkBlankLines2.setLayoutData(gd16);
		shrinkBlankLines2.setEnabled(leaveBlankLines2.getSelection());
		shrinkBlankLines2.addSelectionListener(listener);

		leaveBlankLines3 = new Button(optMisc, SWT.CHECK);
		leaveBlankLines3.setText(
				Messages.getString("Formatting.LeaveBlankLines3.caption")); //$NON-NLS-1$
		leaveBlankLines3.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				shrinkBlankLines3.setEnabled(leaveBlankLines3.getSelection());
				preview();
			}
		});
		shrinkBlankLines3 = new Button(optMisc, SWT.CHECK);
		shrinkBlankLines3.setText(
				Messages.getString("Formatting.ShrinkBlankLines3.caption")); //$NON-NLS-1$
		shrinkBlankLines3.setLayoutData(gd16);
		shrinkBlankLines3.setEnabled(leaveBlankLines3.getSelection());
		shrinkBlankLines3.addSelectionListener(listener);

		alignDoubleArrow = new Button(optMisc, SWT.CHECK);
		alignDoubleArrow.setText(
				Messages.getString("Formatting.AlignDoubleArrow.caption")); //$NON-NLS-1$
		alignDoubleArrow.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				alignDoubleArrowWithSpace.setEnabled(alignDoubleArrow.getSelection());
				alignDoubleArrowWithTab.setEnabled(alignDoubleArrow.getSelection());
				preview();
			}
		});
		alignDoubleArrowWithSpace = new Button(optMisc, SWT.RADIO);
		alignDoubleArrowWithSpace.setText(
				Messages.getString("Formatting.AlignDoubleArrowWithSpace.caption")); //$NON-NLS-1$
		alignDoubleArrowWithSpace.setLayoutData(gd16);
		alignDoubleArrowWithSpace.setEnabled(alignDoubleArrow.getSelection());
		alignDoubleArrowWithSpace.addSelectionListener(listener);
		alignDoubleArrowWithTab = new Button(optMisc, SWT.RADIO);
		alignDoubleArrowWithTab.setText(
				Messages.getString("Formatting.AlignDoubleArrowWithTab.caption")); //$NON-NLS-1$
		alignDoubleArrowWithTab.setLayoutData(gd16);
		alignDoubleArrowWithTab.setEnabled(alignDoubleArrow.getSelection());
		alignDoubleArrowWithTab.addSelectionListener(listener);

		expMisc.setClient(optMisc);

		innerFrame.pack(true);
		outerFrame.pack(true);
		Point innerSize = innerFrame.getSize();
		Point outerSize = outerFrame.getSize();
		int y = innerSize.y - outerSize.y;
		if (y > 0) {
			Point location = innerFrame.getLocation();
			scrollBar.setValues(-location.y, 0, y, y/10, 12, 48);
		} else {
			scrollBar.setEnabled(false);
		}

		composite.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				formatting.layout(true);
				Point location = innerFrame.getLocation();
				innerFrame.layout(true);
				outerFrame.layout(true);
				Point innerSize = innerFrame.getSize();
				Point outerSize = outerFrame.getSize();
				int y = innerSize.y - outerSize.y;
				if (y > 0) {
					scrollBar.setValues(-location.y, 0, y, y/10, 12, 48);
					scrollBar.setEnabled(true);
					innerFrame.setLocation(location);
				} else {
					scrollBar.setEnabled(false);
				}
			}
		});

		Composite preview = new Composite(composite, SWT.NONE);
		preview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		preview.setLayout(new GridLayout(1, true));
		StructuredTextViewer viewer = new PHPStructuredTextViewer(
				preview, null, null, false,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
		document = StructuredModelManager.getModelManager()
				.createStructuredDocumentFor(ContentTypeIdForPHP.ContentTypeID_PHP);
		StyledText text = viewer.getTextWidget();
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		((GridData) text.getLayoutData()).heightHint = 120;
		text.setEditable(false);
		text.setFont(JFaceResources.getFont("org.eclipse.wst.sse.ui.textfont")); //$NON-NLS-1$
		viewer.configure(new PHPStructuredTextViewerConfiguration());
		viewer.setEditable(false);
		viewer.setDocument(document);

		useProjectSpecificSettings(hasProjectSpecificOptions(project));
		initData();
		preview();

		return base;
	}

	public void dispose() {
	}

	public String getDescription() {
		return Messages.getString("Description"); //$NON-NLS-1$
	}

	public void performApply() {
		performOk();
	}

	public void performDefaults() {
		boolean tab = pref.getDefaultBoolean(PreferenceConstants.INDENT_WITH_TAB);
		indentWithTab.setSelection(tab);
		indentWithSpace.setSelection(!tab);
		spaceAmount.setSelection(pref.getDefaultInt(PreferenceConstants.INDENT_SPACES));
		baseIndent.setSelection(pref.getDefaultInt(PreferenceConstants.INDENT_BASE));
		lineLength.setSelection(pref.getDefaultInt(PreferenceConstants.LINE_LENGTH));

		Iterator<FormattingOption> it = formattingOptions.iterator();
		while (it.hasNext()) {
			it.next().performDefaults();
		}

		leaveNewLineWithConcatOp.setSelection(pref.getDefaultBoolean(PreferenceConstants.LEAVE_NEWLINE_WITH_CONCAT_OP));
		joinConcatOpToPrevLine.setSelection(pref.getDefaultBoolean(PreferenceConstants.JOIN_CONCAT_OP_TO_PREV_LINE));
		joinConcatOpToPostLine.setSelection(pref.getDefaultBoolean(PreferenceConstants.JOIN_CONCAT_OP_TO_POST_LINE));
		boolean enabled = leaveNewLineWithConcatOp.getSelection();
		joinConcatOpToPrevLine.setEnabled(enabled);
		joinConcatOpToPostLine.setEnabled(enabled);
		leaveBlankLines1.setSelection(pref.getDefaultBoolean(PreferenceConstants.LEAVE_BLANK_LINES1));
		shrinkBlankLines1.setSelection(pref.getDefaultBoolean(PreferenceConstants.SHRINK_BLANK_LINES1));
		shrinkBlankLines1.setEnabled(leaveBlankLines1.getSelection());
		leaveBlankLines2.setSelection(pref.getDefaultBoolean(PreferenceConstants.LEAVE_BLANK_LINES2));
		shrinkBlankLines2.setSelection(pref.getDefaultBoolean(PreferenceConstants.SHRINK_BLANK_LINES2));
		shrinkBlankLines2.setEnabled(leaveBlankLines2.getSelection());
		leaveBlankLines3.setSelection(pref.getDefaultBoolean(PreferenceConstants.LEAVE_BLANK_LINES3));
		shrinkBlankLines3.setSelection(pref.getDefaultBoolean(PreferenceConstants.SHRINK_BLANK_LINES3));
		shrinkBlankLines3.setEnabled(leaveBlankLines3.getSelection());
		leaveNewLineForArrow.setSelection(pref.getDefaultBoolean(PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW));
		leaveNewLineForArrowNest.setSelection(pref.getDefaultBoolean(PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW_NEST));
		leaveNewLineForArrowNest.setEnabled(leaveNewLineForArrow.getSelection());
		alignDoubleArrow.setSelection(pref.getDefaultBoolean(PreferenceConstants.ALIGN_DOUBLE_ARROW));
		alignDoubleArrowWithSpace.setSelection(!pref.getDefaultBoolean(PreferenceConstants.ALIGN_DOUBLE_ARROW_WITH_TAB));
		alignDoubleArrowWithTab.setSelection(pref.getDefaultBoolean(PreferenceConstants.ALIGN_DOUBLE_ARROW_WITH_TAB));
		alignDoubleArrowWithSpace.setEnabled(alignDoubleArrow.getSelection());
		alignDoubleArrowWithTab.setEnabled(alignDoubleArrow.getSelection());

		preview();
	}

	public boolean performOk() {
		buildStore(pref);

		if (project != null) {
			if (useProject) {
				pref.setValue(PreferenceConstants.PROJECT, project.getName());
				try {
					((ScopedPreferenceStore) pref).save();
				} catch (IOException e) {
					e.printStackTrace();
				}
				reflectToCore(project, true);
			} else {
				removeProjectSpecificSettings();
				reflectToCore(project, false);
			}
		}
		if (project == null || !useProject) {
			reflectToCore(null, true);
		}

		return true;
	}

	public void useProjectSpecificSettings(boolean useProjectSpecificSettings) {
		useProject = useProjectSpecificSettings;
		if (useProjectSpecificSettings && project != null) {
			pref = getScopedPreferenceStore(project);
			if (pref.getString(PreferenceConstants.PROJECT).length() == 0) {
				ScopedPreferenceStore store = new ScopedPreferenceStore(new InstanceScope(), FormatterPrototypePlugin.PLUGIN_ID);
				new PreferenceInitializer().copyPreference(store, pref);
			}
		} else {
			pref = getScopedPreferenceStore(null);
		}
		initData();
	}

	public boolean hasProjectSpecificOptions(IProject project) {
		ScopedPreferenceStore store = getScopedPreferenceStore(project);
		return store.getString(PreferenceConstants.PROJECT).length() > 0;
	}

	public void init(IStatusChangeListener statusChangedListener,
			IProject project, IWorkbenchPreferenceContainer container) {
		this.project = project;
	}

	private class FormattingOption {
		private Button checkbox;
		private String prefKey;

		public FormattingOption(Composite composite, String prefKey, String caption) {
			checkbox = new Button(composite, SWT.CHECK);
			checkbox.setText(Messages.getString(caption));
			this.prefKey = prefKey;
			checkbox.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				public void widgetSelected(SelectionEvent e) {
					preview();
				}
			});
		}

		public void performDefaults() {
			checkbox.setSelection(pref.getDefaultBoolean(prefKey));
		}

		public boolean performOk(IPreferenceStore pref) {
			pref.setValue(prefKey, checkbox.getSelection());
			return true;
		}

		public void initData() {
			checkbox.setSelection(pref.getBoolean(prefKey));
		}
	}

	private void initData() {
		boolean tab = pref.getBoolean(PreferenceConstants.INDENT_WITH_TAB);
		indentWithTab.setSelection(tab);
		indentWithSpace.setSelection(!tab);
		spaceAmount.setSelection(pref.getInt(PreferenceConstants.INDENT_SPACES));
		baseIndent.setSelection(pref.getInt(PreferenceConstants.INDENT_BASE));
		lineLength.setSelection(pref.getInt(PreferenceConstants.LINE_LENGTH));

		Iterator<FormattingOption> it = formattingOptions.iterator();
		while (it.hasNext()) {
			it.next().initData();
		}

		leaveNewLineWithConcatOp.setSelection(pref.getBoolean(PreferenceConstants.LEAVE_NEWLINE_WITH_CONCAT_OP));
		joinConcatOpToPrevLine.setSelection(pref.getBoolean(PreferenceConstants.JOIN_CONCAT_OP_TO_PREV_LINE));
		joinConcatOpToPostLine.setSelection(pref.getBoolean(PreferenceConstants.JOIN_CONCAT_OP_TO_POST_LINE));
		boolean enabled = leaveNewLineWithConcatOp.getSelection();
		joinConcatOpToPrevLine.setEnabled(enabled);
		joinConcatOpToPostLine.setEnabled(enabled);
		leaveBlankLines1.setSelection(pref.getBoolean(PreferenceConstants.LEAVE_BLANK_LINES1));
		shrinkBlankLines1.setSelection(pref.getBoolean(PreferenceConstants.SHRINK_BLANK_LINES1));
		shrinkBlankLines1.setEnabled(leaveBlankLines1.getSelection());
		leaveBlankLines2.setSelection(pref.getBoolean(PreferenceConstants.LEAVE_BLANK_LINES2));
		shrinkBlankLines2.setSelection(pref.getBoolean(PreferenceConstants.SHRINK_BLANK_LINES2));
		shrinkBlankLines2.setEnabled(leaveBlankLines2.getSelection());
		leaveBlankLines3.setSelection(pref.getBoolean(PreferenceConstants.LEAVE_BLANK_LINES3));
		shrinkBlankLines3.setSelection(pref.getBoolean(PreferenceConstants.SHRINK_BLANK_LINES3));
		shrinkBlankLines3.setEnabled(leaveBlankLines3.getSelection());
		leaveNewLineForArrow.setSelection(pref.getBoolean(PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW));
		leaveNewLineForArrowNest.setSelection(pref.getBoolean(PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW_NEST));
		leaveNewLineForArrowNest.setEnabled(leaveNewLineForArrow.getSelection());
		alignDoubleArrow.setSelection(pref.getBoolean(PreferenceConstants.ALIGN_DOUBLE_ARROW));
		alignDoubleArrowWithSpace.setSelection(!pref.getBoolean(PreferenceConstants.ALIGN_DOUBLE_ARROW_WITH_TAB));
		alignDoubleArrowWithTab.setSelection(pref.getBoolean(PreferenceConstants.ALIGN_DOUBLE_ARROW_WITH_TAB));
		alignDoubleArrowWithSpace.setEnabled(alignDoubleArrow.getSelection());
		alignDoubleArrowWithTab.setEnabled(alignDoubleArrow.getSelection());
	}

	private ScopedPreferenceStore getScopedPreferenceStore(IProject project) {
		IScopeContext context;
		if (project == null) {
			context = new InstanceScope();
		} else {
			context = new ProjectScope(project);
		}
		return new ScopedPreferenceStore(context, FormatterPrototypePlugin.PLUGIN_ID);
	}

	private void removeProjectSpecificSettings() {
		ScopedPreferenceStore store = new ScopedPreferenceStore(new ProjectScope(project), FormatterPrototypePlugin.PLUGIN_ID);
		new PreferenceInitializer().resetPreference(store);
		try {
			store.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void preview() {
		if (document != null) {
			IPreferenceStore store = new PreferenceStore();
			buildStore(store);
			IStructuredDocument previewDocument = StructuredModelManager.getModelManager()
					.createStructuredDocumentFor(ContentTypeIdForPHP.ContentTypeID_PHP);
			String previewCode = Messages.getString("Preview.code.default");
			previewDocument.set(previewCode);
			new SimpleContentFormatter().format(previewDocument, new Region(0, 0), store);
			if (!document.get().equals(previewDocument.get())) {
				document.set(previewDocument.get());
			}
		}
	}

	private void buildStore(IPreferenceStore store) {
		store.setValue(PreferenceConstants.INDENT_WITH_TAB, indentWithTab.getSelection());
		store.setValue(PreferenceConstants.INDENT_SPACES, spaceAmount.getSelection());
		store.setValue(PreferenceConstants.INDENT_BASE, baseIndent.getSelection());
		store.setValue(PreferenceConstants.LINE_LENGTH, lineLength.getSelection());

		Iterator<FormattingOption> it = formattingOptions.iterator();
		while (it.hasNext()) {
			it.next().performOk(store);
		}

		store.setValue(PreferenceConstants.LEAVE_NEWLINE_WITH_CONCAT_OP, leaveNewLineWithConcatOp.getSelection());
		store.setValue(PreferenceConstants.JOIN_CONCAT_OP_TO_PREV_LINE, joinConcatOpToPrevLine.getSelection());
		store.setValue(PreferenceConstants.JOIN_CONCAT_OP_TO_POST_LINE, joinConcatOpToPostLine.getSelection());
		store.setValue(PreferenceConstants.LEAVE_BLANK_LINES1, leaveBlankLines1.getSelection());
		store.setValue(PreferenceConstants.SHRINK_BLANK_LINES1, shrinkBlankLines1.getSelection());
		store.setValue(PreferenceConstants.LEAVE_BLANK_LINES2, leaveBlankLines2.getSelection());
		store.setValue(PreferenceConstants.SHRINK_BLANK_LINES2, shrinkBlankLines2.getSelection());
		store.setValue(PreferenceConstants.LEAVE_BLANK_LINES3, leaveBlankLines3.getSelection());
		store.setValue(PreferenceConstants.SHRINK_BLANK_LINES3, shrinkBlankLines3.getSelection());
		store.setValue(PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW, leaveNewLineForArrow.getSelection());
		store.setValue(PreferenceConstants.LEAVE_NEWLINE_FOR_ARROW_NEST, leaveNewLineForArrowNest.getSelection());
		store.setValue(PreferenceConstants.ALIGN_DOUBLE_ARROW, alignDoubleArrow.getSelection());
		store.setValue(PreferenceConstants.ALIGN_DOUBLE_ARROW_WITH_TAB, alignDoubleArrowWithTab.getSelection());

		store.setValue(PHPCoreConstants.FORMATTER_USE_TABS, indentWithTab.getSelection());
		store.setValue(PHPCoreConstants.FORMATTER_INDENTATION_SIZE, spaceAmount.getSelection());
	}

	private void reflectToCore(IProject project, boolean reflect) {
		IScopeContext context;
		if (project == null) {
			context = new InstanceScope();
		} else {
			context = new ProjectScope(project);
		}
		ScopedPreferenceStore store = new ScopedPreferenceStore(context, PHPCoreConstants.PLUGIN_ID);
		if (reflect) {
			boolean useTab = indentWithTab.getSelection();
			store.putValue(PHPCoreConstants.FORMATTER_USE_TABS, Boolean.toString(useTab));
			if (useTab) {
				store.putValue(PHPCoreConstants.FORMATTER_INDENTATION_SIZE,
						PHPCoreConstants.DEFAULT_INDENTATION_SIZE);
			} else {
				store.putValue(PHPCoreConstants.FORMATTER_INDENTATION_SIZE,
						Integer.toString(spaceAmount.getSelection()));
			}
			if (project != null) {
				store.putValue(PreferenceConstants.PROJECT, project.getName());
			}
		} else {
			store.setToDefault(PHPCoreConstants.FORMATTER_USE_TABS);
			store.setToDefault(PHPCoreConstants.FORMATTER_INDENTATION_SIZE);
			store.setToDefault(PreferenceConstants.PROJECT);
		}
		if (project != null) {
			try {
				store.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}