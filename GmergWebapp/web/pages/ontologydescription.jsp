<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Explanatory Note</title>
    <c:choose>
      <c:when test="${proj == 'EuReGene'}">
        <link href="<c:out value="${pageContext.request.contextPath}" />/css/euregene_css.css" type="text/css" rel="stylesheet">
      </c:when>
      <c:otherwise>
        <link href="<c:out value="${pageContext.request.contextPath}" />/css/gudmap_css.css" type="text/css" rel="stylesheet">
      </c:otherwise>
    </c:choose>
    
    <style type="text/css">
    @import("<c:out value="${pageContext.request.contextPath}" />/css/ie51.css");
    </style>  
  </head>
  <body class="evenstripey">
    <h2 class="titletext"><c:out value="${proj}" /> Gene Expression Database</h2>
    <table border="0" height="35">
      <tr align="center"></tr>
    </table>
    <table border="0" cellpadding="0" cellspacing="0">
      <tr class="header-stripey">
        <td><b>Notes on the anatomy ontology</b></td>
      </tr>
      <tr class="header-stripey">
        <td class="plaintext">
<p><b>Anatomy Ontology: </b></p>
<p>A part-of  hierarchy of morphologically distinct structures.</p>

<p><b>An anatomical ontology for annotation of gene expression: </b></p>
<p>Common anatomical terms carry a mixture of connotations, structural, developmental, functional, medical, evolutionary.  Experts communicate quite happily using this variety, but such an inconsistent vocabulary is not sufficient for annotations of gene expression and mutant phenotype on a large scale or in a bioinformatics setting.</p>
<p>We have therefore developed an anatomical ontology for the genitourinary system that aims to be consistent and comprehensive to a level of detail that extends to tissues but not, generally, to cell types. Unlike controlled vocabularies, which simply restrict one to using particular terms, ontologies provide systematic ways to describe entities and their relationships. For example, <i>the renal corpuscle</i> is <i>part of</i> the <i>nephron</i>. Ontologies may contain one or many types of relationship, but our ontology currently uses only the part_of relationship. The advantage of using an ontology for annotation is that, in addition to standardising terms and thus allowing searches, gene expression can be annotated at different levels of resolution and still integrated for the purpose of collation and query.</p>

<p><b>The <c:out value="${proj}" /> ontology based on structural subdivisions: </b></p>
<p>Our ontology is based on morphology rather than gene expression.  The ontology comprises a) a progressive hierarchical subdivision of the GU system (currently only the kidney) into parts and b) some alternative structural subdivisions. The strictly structural basis for our ontology has necessarily resulted in the temporary omission of some important developmental and functional relationships. Our GU anatomy ontology will be incorporated into the Edinburgh Mouse Atlas Project (EMAP) anatomy ontology for the whole mouse. Because the latter is widely used we have tried to minimise the number of deletions of existing terms, unless there was good reason. </p>

<p><b>Rules: </b></p>
<p>The following rules were used: </p>
<p><b>Developmental staging.</b> Theiler staging was used to define the developmental intervals from the early stages of GU development at TS17 to immediately prenatal TS26. Theiler stages refer to the general features of the whole embryo and are thus are only a gross subdivision of developmental time. Within each Theiler stage, finer gradations of development may be represented, either as different stages in the development of the whole organ, or by substructures within the same organ that represent a developmental series.  The terms Stage I to IV nephron are as defined by Reeves W, Caulfield JP, Farquhar MG (1978) Lab Invest 39:90-100 and Larsson L (1975) J Ultrastruct Res 51:119-139</p>

<p><b>Morphological, structural subdivision of parts. </b>Only structures that can be defined morphologically were included. Hence, commonly described features or cell populations may not appear at all as distinct terms. For example, the stroma cannot be histologically distinguished from the rest of the interstitium. </p>
<p>There are three aspects to each term:
a)the fact that such a structural subdivision is recognised
b)what place this is given in the structural hierarchy (what it's part of and what parts it has).
c)Its name(s).</p>
<p>Progressive subdivisions of the existing ontology can easily be accommodated later  (though even these additions change the range of ways we can use - and therefore the meaning of - the previous end-terms in the hierarchy.   c) is not very important from the viewpoint of annotation because annotation terms will actually be given unique identity numbers so their names can be changed or synonyms can be used providing that the name does not play any role in actually defining what the term means.  Therefore, the meaning of each term is defined partly by it's place in the hierarchy and partly by illustrations or definitions that are outside of the hierarchy. </p>
<p>As few individual cell types as possible were described. This does not preclude the insertion of specific cell types as children of existing parent cell types at a later date. Where a specific cell type is well known and important to the literature of the urinary tract (e.g. podocyte), a more generic term for the anatomically visible grouping of these cells was used (e.g. podocyte layer; syn: visceral epithelium). This decision was made in order to remain consistent with the rule relating to a need to be able to distinguish a described element at a histological level.</p>
<p><b>Synonyms. </b>We have listed synonyms for many of the structures. Additional synonyms are easily incorporated. However, it is important that these really do denote the same entity. </p>
<p><b>Definitions. </b>Defining the meaning of terms in the ontology is not trivial and we are currently working on definitions for the terms. In the meantime, we have illustrated the structures denoted by them. </p>
<p><b>Completeness and un-named parts. </b>The entire hierarchy is complete (at least that is our aim). All parts for each structure are described. If any part could not be specifically defined, the term 'rest of' was applied. Hence, if B and C are the only parts of A that are listed, then there are no other parts of A. If, however, B and C are simply the easily recognised parts but there is a residual component of A remaining, it will be defined as D where D = Rest of A. This is necessary to support systematic annotation. </p>
<p><b>Dividing anatomical structure in different ways: 'group' terms. </b>No single hierarchical subdivision captures all the desired structural relationships. Some structures are usefully considered as part of more than one higher-level structure.  For example, the renal corpuscle is part of the cortex; it is also part of the nephron. These different ways of subdividing the organ can be combined so long as, at the finest level, the named substructures are common to the different structural schemes. </p>
<p><b>Method: </b></p>
<p>A 'part_of' anatomical ontology was first created for the murine adult kidney with reference to the Foundational Model Explorer (FME, http://sig.biostr.washington.edu/projects/fm/FME/index.html) and a variety of publications describing the anatomy of the mouse and human kidney. The developmental ontologies were developed by merging this ontology and the existing EMAP terms for urinary tract supplemented by examination of the developing kidney. </p>
<p><b>Use: </b></p>
<p><b>Annotation: inheritance of properties across the 'Part_of' relationship. </b>The 'Part_of' relationship allows us to integrate annotations of gene expression made at different levels of resolution. An example will make this clear. The renal corpuscle is part of the renal cortex.  If gene X is annotated as 'expressed in the renal corpuscle' then it will be returned as positive in response to the query 'list genes expressed in the renal cortex'. If gene Y is annotated as 'expressed' in the renal cortex then it will be returned as 'may be expressed' in response to the query 'list genes expressed in the renal corpuscle'. </p>
<p><b>Knowledge representation. </b>It is worth noting that the ontology is not primarily designed for some aspects of knowledge representation. Later, we may extend its use in this direction by the inclusion of is_a relationships.</p>
<p><b>Developmental relationships: </b></p>
<p>Developmental relationships will be added later by linking each structure to its developmental parents and children. </p>
<p><b>Functional relationships: </b></p>
<p>We have no plans at present to link structures on the basis of common or related functions. This could be done later if need arises. </p>
	</td>
      </tr>                        
    </table>
    <table height="35">
      <tr align="center" class="header-stripey"></tr>
    </table>
    <table>
      <tr>
        <td>
          <input type="button" onclick="self.close();" value="Close">
        </td>
      </tr>
    </table>
  </body>
</html>
